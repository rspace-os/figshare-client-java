package com.researchspace.figshare.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class StringToUrlDeserialiser extends StdDeserializer {

    private static final long serialVersionUID = 1L;

    public StringToUrlDeserialiser() {
        this(null);
    }

    public StringToUrlDeserialiser(Class clazz) {
        super(clazz);
    }

    @Override
    public URL deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode node = objectCodec.readTree(jsonParser);
        String stringUrl = node.asText();
        if (StringUtils.isBlank(stringUrl)) {
            return null;
        } else {
            return new URL(stringUrl);
        }
    }

}
