package com.lee.coroutine.test

import com.lee.coroutine.log
import kotlinx.coroutines.*

/**
 * @author jv.lee
 * @date 2020/3/19
 * @description 2.携程启动模式示例
 *
 *  DEFAULT         立即执行协程体
 *  ATOMIC          立即执行协程体，但在开始运行之前无法取消
 *  UNDISPATCHED    立即在当前线程执行协程体，直到第一个 suspend 调用
 *  LAZY            只有在需要的情况下运行
 */
class PlanBStart {

    fun test() {
        GlobalScope.launch {
            default()
        }
    }

    /**
     *  1.DEFAULT 是饿汉式启动，launch 调用后，会立即进入待调度状态，一旦调度器 OK 就可以开始执行。我们来看个简单的例子：
     *  19:51:08:160 [main] 1
     *  19:51:08:603 [main] 3
     *  19:51:08:606 [DefaultDispatcher-worker-1] 2
     *  19:51:08:624 [main] 4
     */
    private suspend fun default() {
        log("default->")

        log("1")
        val job = GlobalScope.launch(Dispatchers.Default) {
            log("2")
        }
        log("3")
        job.join()
        log("4")
    }

    /**
     * 2.LAZY 是懒汉式启动，launch 后并不会有任何调度行为，协程体也自然不会进入执行状态，直到我们需要它执行的时候。
     * 这其实就有点儿费解了，什么叫我们需要它执行的时候呢？就是需要它的运行结果的时候， launch 调用后会返回一个 Job 实例，对于这种情况，我们可以：
     * 调用 Job.start，主动触发协程的调度执行
     * 调用 Job.join，隐式的触发协程的调度执行
     * 14:56:28:374 [main] 1
     * 14:56:28:493 [main] 3
     * 14:56:28:511 [main] 4
     * 14:56:28:516 [DefaultDispatcher-worker-1] 2
     */
    private suspend fun lazy() {
        log("lazy->")
        log(1)
        val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            log(2)
        }
        log(3)
        job.start()
        log(4)
    }

    /**
     * 3.ATOMIC 只有涉及 cancel 的时候才有意义，cancel 本身也是一个值得详细讨论的话题，在这里我们就简单认为 cancel 后协程会被取消掉，也就是不再执行了。
     * 那么调用 cancel 的时机不同，结果也是有差异的，例如协程调度之前、开始调度但尚未执行、已经开始执行、执行完毕等等
     * 20:42:42:783 [main] 1
     * 20:42:42:879 [main] 3
     * 20:42:42:879 [DefaultDispatcher-worker-1] 2
     */
    private suspend fun atomic() {
        log("atomic->")
        log(1)
        val job = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
            log(2)
        }
        job.cancel()
        log(3)
    }

    /**
     * 4.UNDISPATCHED 就很容易理解了。
     * 协程在这种模式下会直接开始在当前线程下执行，直到第一个挂起点，这听起来有点儿像前面的 ATOMIC，不同之处在于 UNDISPATCHED 不经过任何调度器即开始执行协程体。
     * 当然遇到挂起点之后的执行就取决于挂起点本身的逻辑以及上下文当中的调度器了。
     * 22:00:31:693 [main] 1
     * 22:00:31:782 [main @coroutine#1] 2
     * 22:00:31:800 [main] 4
     * 22:00:31:914 [DefaultDispatcher-worker-1 @coroutine#1] 3
     * 22:00:31:916 [DefaultDispatcher-worker-1 @coroutine#1] 5
     */
    private suspend fun unDispatched() {
        log("unDispatched->")
        log(1)
        val job = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            log(2)
            delay(100)
            log(3)
        }
        log(4)
        job.join()
        log(5)
    }
}