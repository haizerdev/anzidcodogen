# anzidcodogen
custom codogen
[![](https://jitpack.io/v/Fantastic12/anzidcodogen.svg)](https://jitpack.io/#Fantastic12/anzidcodogen)

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
	    repositories {
		maven { url 'https://jitpack.io' }
	    }
	}
  Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.Fantastic12:anzidcodogen:0.1'
	}
	
Step 3. Please add the Kotlin-generated files folder to your source set. 
This is done via sourceSets block in your module’s build.gradle. 
The srcDir is the path to which the files were generated which was indicated by the kapt.kotlin.generated option.
If you do not add this block, you’ll be flagged by a method not found error message in the IDE.

    android {
	…
	sourceSets {
	    main {
                java {
                    srcDir "${buildDir.absolutePath}/generated/source/kaptKotlin/"
                }
            }
        }
    }
