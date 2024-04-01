from conan import ConanFile
from conan.tools.cmake import cmake_layout

class MyAppConan(ConanFile):
    settings = "os", "compiler", "build_type", "arch"
    generators = "CMakeDeps", "CMakeToolchain"
    
    def requirements(self):
        self.requires("zlib/1.2.12")
    
    def layout(self):
        self.folders.build_folder_vars = ["settings.os", "settings.compiler", "settings.compiler.version", "settings.arch", "settings.build_type"] 
        cmake_layout(self)
