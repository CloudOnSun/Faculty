#pragma once
#include <vector>
#include <string>

using namespace std;

class Produs
{
private:

	int id;
	string nume;
	string tip;
	double pret;

public:

	Produs(int id, string nume, string tip, double pret) : id{ id }, nume{ nume }, tip{ tip }, pret{ pret } { }
	int getId()
	{
		return id;
	}
	string getNume()
	{
		return nume;
	}
	string getTip()
	{
		return tip;
	}
	double getPret()
	{
		return pret;
	}
	void valideaza()
	{
		string err = "";
		if (nume.length() == 0)
		{
			err = err + "Nume vid; ";
		}
		if (pret < 1 || pret > 100)
		{
			err = err + "Pret invalid; ";
		}
		if (err.length() > 0)
		{
			throw(err);
		}
	}

	string toString()
	{
		return to_string(id) + "," + nume + "," + tip + "," + to_string(pret);
	}
};