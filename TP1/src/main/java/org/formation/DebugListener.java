package org.formation;

import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.stereotype.Component;

@Component
public class DebugListener {

    @AfterRead
    public void afterRead(Object item) {
        System.out.println(item);
    }
}
