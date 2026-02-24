package com.minimal.jezz.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public final class FontAtlasGenerator {

    private static final class Glyph {
        int id;
        int x;
        int y;
        int w;
        int h;
        int xAdvance;
    }

    private FontAtlasGenerator() {
    }

    private static int nextPow2(int v) {
        int p = 1;
        while (p < v) {
            p <<= 1;
        }
        return p;
    }

    private static void generate(String ttfPath, String outDir, String baseName, int fontSize, int pad, int maxWidth)
            throws Exception {
        Font base = Font.createFont(Font.TRUETYPE_FONT, new File(ttfPath)).deriveFont((float) fontSize);

        BufferedImage probe = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D probeGraphics = probe.createGraphics();
        probeGraphics.setFont(base);
        probeGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        probeGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics metrics = probeGraphics.getFontMetrics();
        int lineHeight = metrics.getHeight() + pad * 2;
        int baseLine = metrics.getAscent() + pad;
        probeGraphics.dispose();

        ArrayList<Glyph> glyphs = new ArrayList<>();
        int x = 1;
        int y = 1;
        int rowHeight = 0;
        int usedWidth = 0;

        for (int id = 32; id <= 255; id++) {
            if (Character.isISOControl(id)) {
                continue;
            }
            char ch = (char) id;
            int advance = Math.max(1, metrics.charWidth(ch));
            int glyphW = Math.max(2, advance + pad * 2);
            int glyphH = lineHeight;
            if (x + glyphW + 1 > maxWidth) {
                x = 1;
                y += rowHeight + 1;
                rowHeight = 0;
            }

            Glyph glyph = new Glyph();
            glyph.id = id;
            glyph.x = x;
            glyph.y = y;
            glyph.w = glyphW;
            glyph.h = glyphH;
            glyph.xAdvance = advance + pad;
            glyphs.add(glyph);

            x += glyphW + 1;
            usedWidth = Math.max(usedWidth, x + 1);
            rowHeight = Math.max(rowHeight, glyphH);
        }

        int atlasWidth = nextPow2(Math.min(maxWidth, usedWidth + 2));
        int atlasHeight = nextPow2(y + rowHeight + 2);

        BufferedImage atlas = new BufferedImage(atlasWidth, atlasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = atlas.createGraphics();
        graphics.setColor(new Color(0, 0, 0, 0));
        graphics.fillRect(0, 0, atlasWidth, atlasHeight);
        graphics.setFont(base);
        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        for (Glyph glyph : glyphs) {
            graphics.drawString(String.valueOf((char) glyph.id), glyph.x + pad, glyph.y + baseLine);
        }
        graphics.dispose();

        File outputDir = new File(outDir);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File pngFile = new File(outputDir, baseName + ".png");
        ImageIO.write(atlas, "png", pngFile);

        File fntFile = new File(outputDir, baseName + ".fnt");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(fntFile), StandardCharsets.UTF_8)) {
            writer.write(
                    "info face=\"" + base.getFontName().replace("\"", "") + "\" size=" + fontSize
                            + " bold=0 italic=0 charset=\"\" unicode=1 stretchH=100 smooth=1 aa=1 padding=" + pad + ","
                            + pad + "," + pad + "," + pad + " spacing=1,1\n");
            writer.write("common lineHeight=" + lineHeight + " base=" + baseLine + " scaleW=" + atlasWidth + " scaleH="
                    + atlasHeight + " pages=1 packed=0\n");
            writer.write("page id=0 file=\"" + baseName + ".png\"\n");
            writer.write("chars count=" + glyphs.size() + "\n");
            for (Glyph glyph : glyphs) {
                writer.write(
                        "char id=" + glyph.id + " x=" + glyph.x + " y=" + glyph.y + " width=" + glyph.w + " height="
                                + glyph.h + " xoffset=0 yoffset=0 xadvance=" + glyph.xAdvance + " page=0 chnl=15\n");
            }
        }

        System.out.println("Generated " + fntFile.getAbsolutePath());
        System.out.println("Generated " + pngFile.getAbsolutePath());
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 12) {
            System.err.println(
                    "Usage: <ttf1> <outDir1> <name1> <size1> <pad1> <maxWidth1> <ttf2> <outDir2> <name2> <size2> <pad2> <maxWidth2>");
            System.exit(1);
        }
        generate(args[0], args[1], args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]),
                Integer.parseInt(args[5]));
        generate(args[6], args[7], args[8], Integer.parseInt(args[9]), Integer.parseInt(args[10]),
                Integer.parseInt(args[11]));
    }
}
