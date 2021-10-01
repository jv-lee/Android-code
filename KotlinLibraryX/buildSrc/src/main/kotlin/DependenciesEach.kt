import dependencies.ProcessorsDependencies
import dependencies.Dependencies

object DependenciesEach {

    val processors = arrayOf(
        ProcessorsDependencies.annotation,
        ProcessorsDependencies.room,
        ProcessorsDependencies.glide,
        ProcessorsDependencies.autoService
    )

    val support = arrayOf(
        Dependencies.coreKtx,

        Dependencies.coroutines,
        Dependencies.coroutinesAndroid,

        Dependencies.lifecycle,
        Dependencies.lifecycleLivedata,
        Dependencies.lifecycleViewModel,

        Dependencies.activity,
        Dependencies.fragment,

        Dependencies.multidex,

        Dependencies.appcompat,
        Dependencies.support,
        Dependencies.recyclerview,
        Dependencies.constraint,
        Dependencies.viewpager2,
        Dependencies.material,

        Dependencies.navigationFragment,
        Dependencies.navigationUi,

        Dependencies.room,
        Dependencies.roomRuntime,

        Dependencies.glide,
        Dependencies.glideOkhttp3,
        Dependencies.glideAnnotations,

        Dependencies.retrofit,
        Dependencies.retrofitConverterGson,
        Dependencies.retrofitConverterScalars,
        Dependencies.retrofitConverterProtobuf,

        Dependencies.autoService,

        Dependencies.imageTools,
    )
}