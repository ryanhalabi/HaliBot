include_directories(
  /usr/local/include 
)

add_library(bot SHARED
  Source/Dll.cpp
  Source/ExampleAIModule.cpp
  )

target_link_libraries(bot /usr/local/lib/libBWAPILIB.dylib)
