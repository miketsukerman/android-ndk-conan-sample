# android-ndk-conan-sample

Sample project to play with ndk and conan

# dependencies 

Install NDK 

https://developer.android.com/studio/projects/install-ndk

Install conan using pip

```bash
pip3 install conan
```

Configure conan

```bash
conan profile detect --force
```

Add android profile

```bash
cat <<EOF > ~/.conan2/profiles/android
  include(default)

  [settings]
  os=Android
  os.api_level=21
  compiler=clang
  compiler.version=12
  compiler.libcxx=c++_static
  compiler.cppstd=14

  [conf]
  tools.android:ndk_path=<path-to-android-ndk>
EOF
```

# build 

```bash
./gradlew installConan
./gradlew build
```

# links

- https://docs.conan.io/2.0/examples/cross_build/android/android_studio.html
