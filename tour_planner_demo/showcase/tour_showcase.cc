#include <algorithm>
#include <cassert>
#include <iomanip>
#include <iostream>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <vector>

using namespace std;

unordered_map<string, int> placeLengthMap = {
    {"Boston", 6 + 10 + 11},       {"Worcester", 9 + 10 + 11},
    {"Springfield", 11 + 10 + 10}, {"Lowell", 6 + 10 + 11},
    {"Cambridge", 9 + 10 + 11},    {"New Bedford", 11 + 10 + 10},
    {"Brockton", 8 + 10 + 11},     {"Quincy", 6 + 10 + 11},
    {"Lynn", 4 + 9 + 11},          {"Fall River", 10 + 10 + 11},
    {"Newton", 6 + 10 + 11},       {"Lawrence", 8 + 10 + 11},
    {"Somerville", 10 + 10 + 11},  {"Framingham", 10 + 10 + 10},
    {"Haverhill", 9 + 10 + 11},    {"Waltham", 7 + 10 + 11},
    {"Malden", 6 + 10 + 10},       {"Brookline", 9 + 10 + 11},
    {"Plymouth", 8 + 10 + 11},     {"Medford", 7 + 10 + 11},
    {"Taunton", 7 + 9 + 11},       {"Chicopee", 8 + 10 + 11},
    {"Weymouth", 8 + 10 + 11},     {"Revere", 6 + 10 + 11},
    {"Peabody", 7 + 10 + 11}};

vector<string> places = {
    "Boston",      "Worcester", "Springfield", "Lowell",     "Cambridge",
    "New Bedford", "Brockton",  "Quincy",      "Lynn",       "Fall River",
    "Newton",      "Lawrence",  "Somerville",  "Framingham", "Haverhill",
    "Waltham",     "Malden",    "Brookline",   "Plymouth",   "Medford",
    "Taunton",     "Chicopee",  "Weymouth",    "Revere",     "Peabody"};
vector<string> combination;
unordered_map<int, vector<vector<string>>> cntMap;

unordered_map<string, vector<string>> farthestMap = {
    {"Boston", {"Chicopee", "Springfield", "New Bedford"}},
    {"Worcester", {"New Bedford", "Plymouth", "Fall River"}},
    {"Springfield", {"New Bedford", "Plymouth", "Fall River"}},
    {"Lowell", {"Chicopee", "Springfield", "New Bedford"}},
    {"Cambridge", {"Chicopee", "Springfield", "New Bedford"}},
    {"New Bedford", {"Chicopee", "Springfield", "Haverhill"}},
    {"Brockton", {"Chicopee", "Springfield", "Haverhill"}},
    {"Quincy", {"Chicopee", "Springfield", "Worcester"}},
    {"Lynn", {"Chicopee", "Springfield", "New Bedford"}},
    {"Fall River", {"Chicopee", "Springfield", "Haverhill"}},
    {"Newton", {"Chicopee", "Springfield", "New Bedford"}},
    {"Lawrence", {"Chicopee", "Springfield", "New Bedford"}},
    {"Somerville", {"Chicopee", "Springfield", "New Bedford"}},
    {"Framingham", {"Chicopee", "Springfield", "New Bedford"}},
    {"Haverhill", {"Chicopee", "Springfield", "New Bedford"}},
    {"Waltham", {"Chicopee", "Springfield", "New Bedford"}},
    {"Malden", {"Chicopee", "Springfield", "New Bedford"}},
    {"Brookline", {"Chicopee", "Springfield", "New Bedford"}},
    {"Plymouth", {"Chicopee", "Springfield", "Haverhill"}},
    {"Medford", {"Chicopee", "Springfield", "New Bedford"}},
    {"Taunton", {"Chicopee", "Springfield", "Haverhill"}},
    {"Chicopee", {"New Bedford", "Plymouth", "Fall River"}},
    {"Weymouth", {"Chicopee", "Springfield", "Worcester"}},
    {"Revere", {"Chicopee", "Springfield", "New Bedford"}},
    {"Peabody", {"Chicopee", "Springfield", "New Bedford"}}};

unordered_map<string, vector<string>> nearestMap = {
    {"Boston", {"Cambridge", "Somerville", "Medford"}},
    {"Worcester", {"Framingham", "Newton", "Waltham"}},
    {"Springfield", {"Chicopee", "Worcester", "Framingham"}},
    {"Lowell", {"Lawrence", "Haverhill", "Waltham"}},
    {"Cambridge", {"Somerville", "Boston", "Medford"}},
    {"New Bedford", {"Fall River", "Taunton", "Brockton"}},
    {"Brockton", {"Quincy", "Weymouth", "Taunton"}},
    {"Quincy", {"Weymouth", "Boston", "Brookline"}},
    {"Lynn", {"Revere", "Peabody", "Malden"}},
    {"Fall River", {"New Bedford", "Taunton", "Brockton"}},
    {"Newton", {"Waltham", "Brookline", "Cambridge"}},
    {"Lawrence", {"Haverhill", "Lowell", "Medford"}},
    {"Somerville", {"Cambridge", "Medford", "Boston"}},
    {"Framingham", {"Newton", "Waltham", "Brookline"}},
    {"Haverhill", {"Lawrence", "Lowell", "Peabody"}},
    {"Waltham", {"Newton", "Cambridge", "Brookline"}},
    {"Malden", {"Medford", "Revere", "Somerville"}},
    {"Brookline", {"Cambridge", "Boston", "Newton"}},
    {"Plymouth", {"Taunton", "Weymouth", "Quincy"}},
    {"Medford", {"Malden", "Somerville", "Boston"}},
    {"Taunton", {"Fall River", "Brockton", "New Bedford"}},
    {"Chicopee", {"Springfield", "Worcester", "Framingham"}},
    {"Weymouth", {"Quincy", "Boston", "Brockton"}},
    {"Revere", {"Malden", "Boston", "Medford"}},
    {"Peabody", {"Lynn", "Revere", "Malden"}}};

bool isFeasible(const vector<string>& v) {
    auto const& first = v[0];
    auto const& second = v[1];
    auto const& last = v.back();

    auto itr = farthestMap.find(first);
    if (itr != farthestMap.end()) {
        auto& list = itr->second;
        if (v[1] == list[0] || v[1] == list[1] || v[1] == list[2])
            return false;
        if (v[2] == list[0] || v[2] == list[1])
            return false;
        if (v[3] == list[0])
            return false;
    }

    auto itr2 = nearestMap.find(first);
    if (itr2 != nearestMap.end()) {
        auto& list = itr->second;
        if (v[4] == list[0] || v[4] == list[1] || v[4] == list[2])
            return false;
        if (v[3] == list[0] || v[3] == list[1])
            return false;
        if (v[2] == list[0])
            return false;
    }

    return true;
}

int computeItemLen(const string& s) {
    return placeLengthMap.at(s) + 23;
}

int computeTotalLen(const vector<string>& v) {
    int fixedLen = 15 + 51 * v.size();
    int itemsLen = 40;
    for (auto const& place : v) {
        itemsLen += computeItemLen(place);
    }
    itemsLen += computeItemLen(v[0]);
    return fixedLen + itemsLen;
}

void do_calc(const vector<string>& v) {
    int total_len = computeTotalLen(v);
    if (isFeasible(v))
        cntMap[total_len].push_back(v);
}

void go(int offset, int k) {
    if (k == 0) {
        do_calc(combination);
        return;
    }
    for (int i = offset; i <= places.size() - k; ++i) {
        combination.push_back(places[i]);
        go(i + 1, k - 1);
        combination.pop_back();
    }
}

bool is_digits(const string& str) {
    return all_of(str.begin(), str.end(), ::isdigit);
}

void printUsageAndExit(string progName) {
    cout << "Usage: " << progName << " <response length> [-n]\n";
    cout << "Specifiy -n if you only want to print out the number of "
            "possiblities."
         << endl;
    exit(-1);
}

int main(int argc, char** argv) {
    if (argc < 2 || argc > 3)
        printUsageAndExit(argv[0]);
    string respLen = argv[1];
    bool verbose = true;

    if (is_digits(respLen)) {
        if (argc == 3 && string(argv[2]) == "-n")
            verbose = false;
    } else {
        if (respLen == "-n" && argc == 3 && is_digits(argv[2])) {
            respLen = argv[2];
            verbose = false;
        } else
            printUsageAndExit(argv[0]);
    }

    int n = 25;
    int k = 5;

    int len;
    try {
        len = stoi(respLen);
    } catch (const exception& e) {
        cout << "Length parsing failed: " << e.what() << endl;
        exit(-2);
    }

    assert(places.size() == n);
    assert(placeLengthMap.size() == n);
    go(0, k);

    auto itr = cntMap.find(len);
    if (itr == cntMap.end()) {
        cout << "Response of length " << len << " is not possible" << endl;
        exit(-3);
    }

    if (verbose) {
        for (auto const& places : itr->second) {
            cout << "[ ";
            string delim = "";
            for (auto const& place : places) {
                cout << delim << std::setw(11) << place;
                delim = ", ";
            }
            cout << " ]\n";
        }
    }
    cout << "\nTotal number of possibility = " << itr->second.size() << endl;

    return 0;
}
