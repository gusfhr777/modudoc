package com.piltong.modudoc.common.util;


//import org.fxmisc.richtext.model.Codec;
//import org.fxmisc.richtext.model.PlainTextChange;
//import org.fxmisc.richtext.model.ReadOnlyStyledDocument;
//import org.fxmisc.richtext.model.SegmentOps;
//import org.fxmisc.richtext.model.StyledDocument;
//import org.fxmisc.richtext.util.UndoUtils;
//
//import java.io.*;
//
//public class RichTextSerializer {
//
//    // 빈 스타일용 Codec
//    private static final Codec<Void> voidCodec = new Codec<>() {
//        @Override
//        public Void decode(DataInputStream is) {
//            return null;
//        }
//
//        @Override
//        public void encode(DataOutputStream os, Void v) {
//            // nothing
//        }
//
//        @Override
//        public String getName() {
//
//            return "";
//        }
//
//    };
//
//    private static final Codec<StyledDocument<Void, String, Void>> CODEC =
//            ReadOnlyStyledDocument.codec(
//                    SegmentOps.styledTextOps(),  // SegmentOps<String, Void>
//                    Codec.STRING_CODEC,          // segment codec (String)
//                    voidCodec                   // paragraph style codec
//            );
//
//    public static byte[] serialize(StyledDocument<Void, String, Void> doc) throws IOException {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        DataOutputStream dataOut = new DataOutputStream(out);
//        CODEC.encode(dataOut, doc);
//        return out.toByteArray();
//    }
//
//    public static StyledDocument<Void, String, Void> deserialize(byte[] data) throws IOException {
//        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
//        try {
//            return CODEC.decode(in);
//        } catch (ClassCastException | IOException e) {
//            throw new IOException("역직렬화 실패", e);
//        }
//    }
//}

