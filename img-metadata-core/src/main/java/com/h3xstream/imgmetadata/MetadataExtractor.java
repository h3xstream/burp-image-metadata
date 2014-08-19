package com.h3xstream.imgmetadata;

import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.chunks.ChunkHelper;
import ar.com.hjg.pngj.chunks.PngChunk;
import ar.com.hjg.pngj.chunks.PngChunkTextVar;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MetadataExtractor {

    public Map<String, String> readMetadata(byte[] respBytes, int bodyOffset) throws ImageProcessingException, IOException {


        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(respBytes, bodyOffset, respBytes.length - bodyOffset));
        Map<String, String> tags = new HashMap<String,String>();

        if(bodyOffset + 8 > respBytes.length) {
            return tags; //Broken image .. return to avoid ArrayIndexOutOfBoundsException
        }

        //Magic codes taken from http://en.wikipedia.org/wiki/Magic_number_(programming)#Examples
        if(isJpgFile(respBytes,bodyOffset)) { //JPEG/Exif

            Metadata metadata = ImageMetadataReader.readMetadata(in, false);

            for (Directory dir : metadata.getDirectories()) {
                for (Tag tag : dir.getTags()) {
                    tags.put(tag.getTagName(),tag.getDescription());
                }
            }
        }
        else if(isPngFile(respBytes,bodyOffset)) {
            PngReader pngr = new PngReader(in);

            pngr.readSkippingAllRows();
            for (PngChunk c : pngr.getChunksList().getChunks()) {
                if (!ChunkHelper.isText(c))   continue;
                PngChunkTextVar ct = (PngChunkTextVar) c;

                tags.put(ct.getKey(),ct.getVal());
            }
        }

        return tags;
    }

    public static boolean isJpgFile(byte[] respBytes,int bodyOffset) {
        return ((respBytes[bodyOffset] == (byte) 0xFF && respBytes[bodyOffset+1] == (byte) 0xD8) //JPEG
                || (respBytes[bodyOffset] == (byte) 0x4A && respBytes[bodyOffset+1] == (byte) 0x46 && respBytes[bodyOffset+2] == (byte) 0x49 && respBytes[bodyOffset+3] == (byte) 0x46) //JPEG/JFIF
                || (respBytes[bodyOffset] == (byte) 0x45 && respBytes[bodyOffset+1] == (byte) 0x78 && respBytes[bodyOffset+2] == (byte) 0x69 && respBytes[bodyOffset+3] == (byte) 0x66));
    }

    public static boolean isPngFile(byte[] respBytes,int bodyOffset) {
        return respBytes[bodyOffset] == (byte) 0x89 && respBytes[bodyOffset+1] == (byte) 0x50 && respBytes[bodyOffset+2] == (byte) 0x4E && //
                respBytes[bodyOffset+3] == (byte) 0x47 && respBytes[bodyOffset+4] == (byte) 0x0D && respBytes[bodyOffset+5] == (byte) 0x0A && //
                respBytes[bodyOffset+6] == (byte) 0x1A && respBytes[bodyOffset+7] == (byte) 0x0A;  // Magic bytes : 0x89 P N G \r \n 0x1a \n
    }
}
