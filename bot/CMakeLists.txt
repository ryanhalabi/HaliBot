include_directories(
  /usr/local/include 
)
 
file(GLOB_RECURSE SOURCES "Source/**cpp")
add_library(bot SHARED ${SOURCES})

target_link_libraries(bot /usr/local/lib/libBWAPILIB.dylib)
