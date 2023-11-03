package mapping_layer.codec_providers;

import mapping_layer.codecs.UUIDCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class UUIDCodecProvider implements CodecProvider {

    public UUIDCodecProvider() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass == UUIDCodec.class) {
            return (Codec<T>) new UUIDCodec(codecRegistry);
        }

        return null;
    }
}
