# STAC-engagement-src

Each sub-directory under the root contains one challenge program.

Each challenge program directory is structured as follows:
* `description.txt` is a text file that describes what the challenge program does. This should always be the first file one needs to read before exploring anything else.
* `challenge_program` directory contains bytecodes and data of the challenge program. For sever programs, it should also contain a convenient script `startServer.sh`, which can be used to start the server program.
* `examples` directory contains various kinds of convenient scripts. Their uses can usually be found in `description.txt`. For server programs, those scripts are mainly used to show how one could interact with the server after the server is started. 
* `questions` directory contains all questions one needs to answer. Those questions will tell you what type(s) of vulnerabilities the corresponding challenge program may have as well as the budget for exploitation. From our previous experience, it is usually the case that the answer to at least one of the questions should be a "yes".
* `decompile` directory contains the decompiled source codes we got from the challenge_program bytecodes. Note that we've fixed the decompiled sources so that it is possible to compile them back to bytecodes. Those source codes are mainly there for ease of code reading and debugging. 
* `APIsUsed.txt` is a list of all libraries and external APIs used by the challenge program. It is valuable information for those of you who want to run static analyzer on the corresponding challenge programs. 

Have fun, and good luck hacking! 
