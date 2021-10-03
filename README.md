# Anzidcodogen
[![](https://jitpack.io/v/haizerdev/anzidcodogen.svg)](https://jitpack.io/#haizerdev/anzidcodogen)

Annotation proccessor for generate public observers

Supported annotation PublicSharedFlow, PublicLiveData, InternalSharedFlow, InternalLiveData

Example:

![image](https://user-images.githubusercontent.com/46586567/135762439-e910d9ec-f226-4013-a3f7-76a333c95bdd.png)

![image](https://user-images.githubusercontent.com/46586567/135758163-657b2a5a-19bb-49b4-a1e3-a2b0378895b8.png)


Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
	    repositories {
		maven { url 'https://jitpack.io' }
	    }
	}
  Step 2. Add the dependency

	dependencies {
	     implementation 'com.github.haizerdev:anzidcodogen:0.3'
	     kapt 'com.github.haizerdev:anzidcodogen:0.3'
	}
	
Step 3. Please add the Kotlin-generated files folder to your source set. 
This is done via sourceSets block in your module’s build.gradle. 
The srcDir is the path to which the files were generated which was indicated by the kapt.kotlin.generated option.
If you do not add this block, you’ll be flagged by a method not found error message in the IDE.

    android {
      ...
         sourceSets {
             main {
                 java {
                     srcDir "${buildDir.absolutePath}/generated/source/kaptKotlin/"
                 }
             }
         }
    }
