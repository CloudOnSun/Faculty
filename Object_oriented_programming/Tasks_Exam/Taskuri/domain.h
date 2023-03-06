#pragma once
#include <string>
#include <vector>
using namespace std;

class Task
{
private:

	int id;
	string descriere;
	vector <string> programatori;
	string stare;

public:

	Task(int id, string d, vector<string> p, string s) : id{ id }, descriere{ d }, programatori{ p }, stare{ s } { }

	void valideaza()
	{
		if (descriere.length() == 0)
			throw (1);
		if (stare != "open" && stare != "inprogress" && stare != "closed")
			throw (1);
		if (programatori.size() < 1 || programatori.size() > 4)
			throw (1);
	}

	string toString()
	{
		string p;
		for (auto p2 : programatori)
		{
			p = p + "," + p2;
		}
		return to_string(id) + "," + descriere + "," + stare + p;
	}

	int getId()
	{
		return id;
	}

	string getStare()
	{
		return stare;
	}

	string getDesc()
	{
		return descriere;
	}

	vector<string> getPro()
	{
		return programatori;
	}

	string getPro2()
	{
		string p = programatori.at(0);
		for (int i = 1; i < programatori.size(); i++)
		{
			p = p + "," + programatori.at(i);
		}
		return p;
	}

	void setStare(string s)
	{
		stare = s;
	}
};