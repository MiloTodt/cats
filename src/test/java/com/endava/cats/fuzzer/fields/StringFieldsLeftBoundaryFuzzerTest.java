package com.endava.cats.fuzzer.fields;

import com.endava.cats.io.ServiceCaller;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class StringFieldsLeftBoundaryFuzzerTest {

    @Mock
    private ServiceCaller serviceCaller;

    @Mock
    private TestCaseListener testCaseListener;

    @Mock
    private CatsUtil catsUtil;

    private StringFieldsLeftBoundaryFuzzer stringFieldsLeftBoundaryFuzzer;

    @BeforeEach
    void setup() {
        stringFieldsLeftBoundaryFuzzer = new StringFieldsLeftBoundaryFuzzer(serviceCaller, testCaseListener, catsUtil);
    }

    @Test
    void givenANewStringFieldsLeftBoundaryFuzzer_whenCreatingANewInstance_thenTheMethodsBeingOverriddenAreMatchingTheStringFieldsLeftBoundaryFuzzer() {
        NumberSchema nrSchema = new NumberSchema();
        Assertions.assertThat(stringFieldsLeftBoundaryFuzzer.getSchemasThatTheFuzzerWillApplyTo().stream().anyMatch(schema -> schema.isAssignableFrom(StringSchema.class))).isTrue();
        Assertions.assertThat(stringFieldsLeftBoundaryFuzzer.getBoundaryValue(nrSchema)).isNotNull();
        Assertions.assertThat(stringFieldsLeftBoundaryFuzzer.hasBoundaryDefined(nrSchema)).isFalse();
        Assertions.assertThat(stringFieldsLeftBoundaryFuzzer.description()).isNotNull();

        nrSchema.setMinLength(2);
        Assertions.assertThat(stringFieldsLeftBoundaryFuzzer.hasBoundaryDefined(nrSchema)).isTrue();

    }
}
