plugins {
    id("com.android.library")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "1.5.21-1.0.0-beta06"
    kotlin("android")
//    kotlin("android.extensions")
}


android {
    compileSdk = BuildConfig.compileSdkVersion

    defaultConfig {
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion
//        consumerProguardFiles()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    productFlavors {
       //https://developer.android.google.cn/studio/build/build-variants
    }

    buildTypes {
        getByName("debug") {
            extra["alwaysUpdateBuildId"] = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

//https://github.com/android/user-interface-samples/tree/master
//https://developer.android.google.cn/kotlin/ktx?hl=zh-cn
dependencies {
    kapt("com.android.databinding:compiler:3.1.4")

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
//    implementation("androidx.window:window:1.0.0-beta01") //minCompileSdk (31)
    implementation("androidx.webkit:webkit:1.4.0")

//    https://developer.android.google.cn/kotlin/ktx?hl=zh-cn#kts
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.activity:activity-ktx:1.3.1")
    implementation("androidx.work:work-runtime-ktx:2.5.0")
    implementation("androidx.paging:paging-common-ktx:3.0.1")
    implementation("androidx.paging:paging-runtime-ktx:3.0.1")
    implementation("androidx.collection:collection-ktx:1.1.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.dynamicanimation:dynamicanimation-ktx:1.0.0-alpha03")
    implementation("androidx.core:core-splashscreen:1.0.0-alpha01")

    implementation("androidx.room:room-ktx:2.3.0")
    implementation("androidx.room:room-paging:2.4.0-alpha04")
    ksp("androidx.room:room-compiler:2.3.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.3.1")

    //https://mbonnin.medium.com/the-different-kotlin-stdlibs-explained-83d7c6bf293
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")

    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
