# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.16

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/local/Cellar/cmake/3.16.2/bin/cmake

# The command to remove a file.
RM = /usr/local/Cellar/cmake/3.16.2/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/ryan/starcraft_ai/bot

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/ryan/starcraft_ai/bot/build

# Include any dependencies generated for this target.
include bot/CMakeFiles/bot.dir/depend.make

# Include the progress variables for this target.
include bot/CMakeFiles/bot.dir/progress.make

# Include the compile flags for this target's objects.
include bot/CMakeFiles/bot.dir/flags.make

bot/CMakeFiles/bot.dir/Source/Dll.cpp.o: bot/CMakeFiles/bot.dir/flags.make
bot/CMakeFiles/bot.dir/Source/Dll.cpp.o: ../bot/Source/Dll.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/ryan/starcraft_ai/bot/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object bot/CMakeFiles/bot.dir/Source/Dll.cpp.o"
	cd /Users/ryan/starcraft_ai/bot/build/bot && /Library/Developer/CommandLineTools/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/bot.dir/Source/Dll.cpp.o -c /Users/ryan/starcraft_ai/bot/bot/Source/Dll.cpp

bot/CMakeFiles/bot.dir/Source/Dll.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/bot.dir/Source/Dll.cpp.i"
	cd /Users/ryan/starcraft_ai/bot/build/bot && /Library/Developer/CommandLineTools/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /Users/ryan/starcraft_ai/bot/bot/Source/Dll.cpp > CMakeFiles/bot.dir/Source/Dll.cpp.i

bot/CMakeFiles/bot.dir/Source/Dll.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/bot.dir/Source/Dll.cpp.s"
	cd /Users/ryan/starcraft_ai/bot/build/bot && /Library/Developer/CommandLineTools/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /Users/ryan/starcraft_ai/bot/bot/Source/Dll.cpp -o CMakeFiles/bot.dir/Source/Dll.cpp.s

bot/CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.o: bot/CMakeFiles/bot.dir/flags.make
bot/CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.o: ../bot/Source/ExampleAIModule.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/Users/ryan/starcraft_ai/bot/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object bot/CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.o"
	cd /Users/ryan/starcraft_ai/bot/build/bot && /Library/Developer/CommandLineTools/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.o -c /Users/ryan/starcraft_ai/bot/bot/Source/ExampleAIModule.cpp

bot/CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.i"
	cd /Users/ryan/starcraft_ai/bot/build/bot && /Library/Developer/CommandLineTools/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /Users/ryan/starcraft_ai/bot/bot/Source/ExampleAIModule.cpp > CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.i

bot/CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.s"
	cd /Users/ryan/starcraft_ai/bot/build/bot && /Library/Developer/CommandLineTools/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /Users/ryan/starcraft_ai/bot/bot/Source/ExampleAIModule.cpp -o CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.s

# Object files for target bot
bot_OBJECTS = \
"CMakeFiles/bot.dir/Source/Dll.cpp.o" \
"CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.o"

# External object files for target bot
bot_EXTERNAL_OBJECTS =

lib/libbot.dylib: bot/CMakeFiles/bot.dir/Source/Dll.cpp.o
lib/libbot.dylib: bot/CMakeFiles/bot.dir/Source/ExampleAIModule.cpp.o
lib/libbot.dylib: bot/CMakeFiles/bot.dir/build.make
lib/libbot.dylib: /usr/local/lib/libBWAPILIB.dylib
lib/libbot.dylib: bot/CMakeFiles/bot.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/Users/ryan/starcraft_ai/bot/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Linking CXX shared library ../lib/libbot.dylib"
	cd /Users/ryan/starcraft_ai/bot/build/bot && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/bot.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
bot/CMakeFiles/bot.dir/build: lib/libbot.dylib

.PHONY : bot/CMakeFiles/bot.dir/build

bot/CMakeFiles/bot.dir/clean:
	cd /Users/ryan/starcraft_ai/bot/build/bot && $(CMAKE_COMMAND) -P CMakeFiles/bot.dir/cmake_clean.cmake
.PHONY : bot/CMakeFiles/bot.dir/clean

bot/CMakeFiles/bot.dir/depend:
	cd /Users/ryan/starcraft_ai/bot/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/ryan/starcraft_ai/bot /Users/ryan/starcraft_ai/bot/bot /Users/ryan/starcraft_ai/bot/build /Users/ryan/starcraft_ai/bot/build/bot /Users/ryan/starcraft_ai/bot/build/bot/CMakeFiles/bot.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : bot/CMakeFiles/bot.dir/depend

