package asynctask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class MyTask<INPUT_TYPE, RETURN_TYPE> implements Task<RETURN_TYPE> {

    public CompletableFuture<RETURN_TYPE> future;

    public MyTask(CompletableFuture<RETURN_TYPE> future) {
        this.future = future;
    }


    public static <RETURN_TYPE> Task<RETURN_TYPE> of(Supplier<RETURN_TYPE> actionSupplier) {
        Task<RETURN_TYPE> task = new MyTask<>(CompletableFuture.supplyAsync(actionSupplier));
        return task;
    }

    @Override
    public CompletableFuture<RETURN_TYPE> build() {
        return future;
    }

    @Override
    public Task<RETURN_TYPE> onErrorRecoverWith(Function<Throwable, RETURN_TYPE> recoveryFunction) {
        return new MyTask<>(future.exceptionally(recoveryFunction));
    }

    @Override
    public <NEW_RETURN_TYPE> Task<NEW_RETURN_TYPE> andThenDo(Function<RETURN_TYPE, NEW_RETURN_TYPE> action) {
        Task<NEW_RETURN_TYPE> task = new MyTask<>(future.thenApply(action));
        return task;
    }

    @Override
    public Task<RETURN_TYPE> andThenWait(long number, TimeUnit timeUnit) {
        this.future = this.future.thenCompose((var) -> CompletableFuture.supplyAsync(() -> var , CompletableFuture.delayedExecutor(number, timeUnit)));
        return this;
    }
}
