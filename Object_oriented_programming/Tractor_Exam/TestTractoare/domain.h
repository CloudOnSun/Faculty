#pragma once
#include <string>
#include <vector>

using namespace std;

class Tractor
{
private:

	int id;
	string denumire;
	string tip;
	int nrRoti;

public:

	Tractor(int id, string den, string tip, int nr) : id{ id }, denumire{ den }, tip{ tip }, nrRoti{ nr } { }

	int getId()
	{
		return id;
	}

	string getDenumire()
	{
		return denumire;
	}

	string getTip()
	{
		return tip;
	}

	int getNr()
	{
		return nrRoti;
	}

	string toString()
	{
		return to_string(id) + "," + denumire + "," + tip + "," + to_string(nrRoti);
	}

	void valideaza()
	{
		string err = "";
		if (denumire.length() == 0)
		{
			err = err + "Denumire vida ";
		}

		if (tip.length() == 0)
		{
			err = err + "Tip vid ";
		}

		if (nrRoti < 2 || nrRoti > 16 || nrRoti % 2 == 1)
		{
			err = err + "NrRoti invalid ";
		}

		if (err.length() > 0)
		{
			throw(err);
		}
	}

	void setRoti(int r)
	{
		nrRoti = r;
	}
};