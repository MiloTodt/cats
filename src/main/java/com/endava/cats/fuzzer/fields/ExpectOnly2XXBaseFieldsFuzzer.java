package com.endava.cats.fuzzer.fields;

import com.endava.cats.fuzzer.http.ResponseCodeFamily;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;

public abstract class ExpectOnly2XXBaseFieldsFuzzer extends BaseFieldsFuzzer {

    public ExpectOnly2XXBaseFieldsFuzzer(ServiceCaller sc, TestCaseListener lr, CatsUtil cu) {
        super(sc, lr, cu);
    }

    @Override
    protected ResponseCodeFamily getExpectedHttpCodeWhenRequiredFieldsAreFuzzed() {
        return ResponseCodeFamily.TWOXX;
    }

    @Override
    protected ResponseCodeFamily getExpectedHttpCodeWhenOptionalFieldsAreFuzzed() {
        return ResponseCodeFamily.TWOXX;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
