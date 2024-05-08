CXX=g++
WT_BASE=/usr/local
CXXFLAGS=--std=c++17 -I$(WT_BASE)/include -I./include

LDFLAGS=-L$(WT_BASE)/lib -Wl,-rpath,$(WT_BASE)/lib -lwthttp -lwt

DEPS = 
OBJS = main.o qa.o qaSet.o gui.o user.o answerScorer.o

%.o: src/%.cpp $(DEPS)
	$(CXX) $(CXXFLAGS) -c -o $@ $<
gui: $(OBJS)
	$(CXX) $(CXXFLAGS) -o $@ $^ $(LDFLAGS)
clean:
	rm -f gui $(OBJS)
