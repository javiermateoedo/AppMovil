plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.plasticrangers.frontend"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.plasticrangers.frontend"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        // Excluir archivos específicos en META-INF
        resources {
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/LICENSE")
            excludes.add("META-INF/NOTICE")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.tbuonomo:dotsindicator:4.2")
    implementation("androidx.cardview:cardview:<version>")
    implementation("com.google.android.material:material:<version>")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.twilio.sdk:twilio:8.22.0")
    implementation("androidx.activity:activity:1.2.0")
    implementation("androidx.fragment:fragment:1.3.0")
    implementation("com.google.zxing:core:3.4.1")

}


