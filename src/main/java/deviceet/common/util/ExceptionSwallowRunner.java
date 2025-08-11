package deviceet.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionSwallowRunner {

    public static void run(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            log.error("Failed to run: ", ex);
        }
    }


}
