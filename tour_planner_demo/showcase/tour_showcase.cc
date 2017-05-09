#include "cmdparser.hpp"
#include <algorithm>
#include <cassert>
#include <functional>
#include <iomanip>
#include <iostream>
#include <limits>
#include <set>
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

class Tour {
private:
    string start;
    vector<string> cities;

public:
    Tour(const string& start, const vector<string>& cities)
        : start(start), cities(cities) {}

    const string& getStart() const { return start; }
    const vector<string>& getCities() const { return cities; }

    unordered_set<string> getCitySet() const {
        return unordered_set<string>(cities.begin(), cities.end());
    }

    bool operator==(const Tour& rhs) const {
        return start == rhs.start && cities == rhs.cities;
    }
    bool operator!=(const Tour& rhs) const { return !(*this == rhs); }

    size_t size() const { return cities.size(); }
};

struct TourComparator {
    bool operator()(const Tour& lhs, const Tour& rhs) const {
        return lhs.getCities() < rhs.getCities();
    }
};

struct TourEqual {
    bool operator()(const Tour& lhs, const Tour& rhs) const {
        return lhs.getCities() == rhs.getCities();
    }
};

struct TourHasher {
    size_t operator()(const Tour& tour) const {
        size_t seed = tour.size();
        auto strHasher = hash<string>();
        for (auto& city : tour.getCities()) {
            seed ^= strHasher(city) + 0x9e3779b9 + (seed << 6) + (seed >> 2);
        }
        return seed;
    }
};

vector<string> combination;
unordered_map<int, vector<Tour>> cntMap;

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

bool isFeasible(const Tour& tour) {
    /*    auto const& first = v[0];
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
    */
    return true;
}

int computeItemLen(const string& s) {
    return placeLengthMap.at(s) + 25;
}

int computeTotalLen(const Tour& tour) {
    int fixedLen = 15 + 51 * tour.size();
    int itemsLen = 23 + tour.size();
    for (auto const& place : tour.getCities()) {
        itemsLen += computeItemLen(place);
    }
    itemsLen += computeItemLen(tour.getStart());
    return fixedLen + itemsLen;
}

void do_calc(const vector<string>& v) {
    for (auto const& place : v) {
        Tour tour(place, v);
        int total_len = computeTotalLen(tour);
        if (isFeasible(tour))
            cntMap[total_len].push_back(tour);
    }
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

void configure_parser(cli::Parser& parser) {
    parser.set_optional<vector<string>>(
        "c", "cities", vector<string>(),
        "List of cities to check if it is included in the solution");
    parser.set_optional<bool>("s", "silent", false,
                              "Only print the number of possibilities");
    parser.set_optional<int>("k", "numcities", 5,
                             "Number of cities in the tour");
    parser.set_required<int>("l", "resp-len", "Length of the response");
}

void printCities(const std::vector<string>& cities) {
    cout << "{ ";
    string delim = "";
    for (auto const& place : cities) {
        cout << delim << std::setw(11) << place;
        delim = ", ";
    }
    cout << " }\n";
}

void printTour(const Tour& tour) {
    string startString = tour.getStart() + "->";
    cout << setw(13) << startString;
    printCities(tour.getCities());
}

int main(int argc, char** argv) {
    cli::Parser parser(argc, argv);
    configure_parser(parser);
    parser.run_and_exit_if_error();

    int n = 25;
    int k = parser.get<int>("k");
    int len = parser.get<int>("l");
    bool verbose = !parser.get<bool>("s");
    if (k <= 1 || k > 25) {
        cout << "Illegal number of cities: " << k << endl;
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

    unordered_set<Tour, TourHasher, TourEqual> tourSet;
    int numSol = 0;
    for (auto const& tour : itr->second) {
        if (tourSet.insert(tour).second) {
            if (verbose)
                printCities(tour.getCities());
            ++numSol;
        }
    }
    if (verbose) {
        int minLen = numeric_limits<int>::max();
        int maxLen = 0;
        for (auto const& mapping : cntMap) {
            auto len = mapping.first;
            maxLen = max(len, maxLen);
            minLen = min(len, minLen);
        }
        cout << "\nMax possible response size for k=" << k << " is " << maxLen
             << '\n';
        cout << "Min possible response size for k=" << k << " is " << minLen
             << endl;
    }

    cout << "\nTotal number of possibility for response size " << len << " = "
         << numSol << endl;

    auto cities = parser.get<vector<string>>("c");
    if (!cities.empty()) {
        if (cities.size() != k) {
            cout << "ERROR: must specify " << k
                 << " cities to perform the check\n";
            exit(-4);
        }
        unordered_set<string> citySet(cities.begin(), cities.end());

        bool found = false;
        for (auto const& tour : itr->second) {
            auto candidate = tour.getCitySet();
            if (candidate == citySet) {
                found = true;
                cout << "FOUND TOUR:\n";
                printTour(tour);
                // break;
            }
        }
        if (!found)
            cout << "Given cities not found in solution\n";
    }

    return 0;
}
