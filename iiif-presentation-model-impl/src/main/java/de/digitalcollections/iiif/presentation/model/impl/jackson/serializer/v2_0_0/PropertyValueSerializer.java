package de.digitalcollections.iiif.presentation.model.impl.jackson.serializer.v2_0_0;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.digitalcollections.iiif.presentation.model.api.v2_0_0.PropertyValue;
import de.digitalcollections.iiif.presentation.model.impl.v2_0_0.PropertyValueLocalizedImpl;
import de.digitalcollections.iiif.presentation.model.impl.v2_0_0.PropertyValueSimpleImpl;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

public class PropertyValueSerializer extends StdSerializer<PropertyValue> {
  public PropertyValueSerializer() {
    this(null);
  }

  public PropertyValueSerializer(Class<PropertyValue> t) {
    super(t);
  }

  private void writeSingleLocalization(JsonGenerator jgen, Locale language, String value) throws IOException {
    jgen.writeStartObject();
    jgen.writeStringField("@language", language.toLanguageTag());
    jgen.writeStringField("@value", value);
    jgen.writeEndObject();
  }

  @Override
  public void serialize(PropertyValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
    if (value instanceof PropertyValueSimpleImpl) {
      jgen.writeString(value.getValue());
    } else {
      PropertyValueLocalizedImpl localized = (PropertyValueLocalizedImpl) value;
      Set<Locale> localizations = localized.getLocalizations();
      if (localizations.size() == 1) {
        writeSingleLocalization(jgen, localizations.iterator().next(),
                                localized.getValue());
      } else {
        jgen.writeStartArray();
        for (Locale language : localizations) {
          writeSingleLocalization(jgen, language, localized.getValue(language));
        }
        jgen.writeEndArray();
      }
    }
  }
}
