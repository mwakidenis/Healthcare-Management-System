package util;

import javax.swing.SwingWorker;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Background {

    // run task that returns T, call onDone with result, call onError on exception
    public static <T> void run(Callable<T> task, Consumer<T> onDone, Consumer<Exception> onError) {
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.call();
            }
            @Override
            protected void done() {
                try {
                    T res = get();
                    if (onDone != null) onDone.accept(res);
                } catch (Exception ex) {
                    if (onError != null) onError.accept(ex);
                }
            }
        }.execute();
    }
}
