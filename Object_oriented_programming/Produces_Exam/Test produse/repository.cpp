#include "repository.h"
#include <fstream>

void RepoProduse::citesteDinFisier()
{
	ifstream in(file);
	produse.clear();
	string linie, nume, tip;
	int id;
	double pret;
	while (getline(in, linie))
	{
		size_t poz = linie.find_first_of(",");
		id = stoi(linie.substr(0, poz));
		linie = linie.substr(poz + 1, linie.length());

		poz = linie.find_first_of(",");
		nume = linie.substr(0, poz);
		linie = linie.substr(poz + 1, linie.length());

		poz = linie.find_first_of(",");
		tip = linie.substr(0, poz);
		linie = linie.substr(poz + 1, linie.length());

		pret = stod(linie);

		Produs p(id, nume, tip, pret);
		produse.push_back(p);
	}
}


void RepoProduse::incarcaInFisier()
{
	ofstream out(file);
	for (auto p : produse)
	{
		out << p.toString() << endl;
	}
}

vector<Produs> RepoProduse::getAll()
{
	return produse;
}

void RepoProduse::adauga(Produs p)
{
	string err = "";
	for (auto each : produse)
	{
		if (each.getId() == p.getId())
		{
			err = "Acelasi Id";
			break;
		}
	}
	if (err.length() > 0)
	{
		throw(err);
	}
	produse.push_back(p);

	incarcaInFisier();
}