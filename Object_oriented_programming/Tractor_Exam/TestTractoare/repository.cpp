#include "repository.h"
#include <fstream>

void RepoTractoare::citesteDinFisier()
{
	ifstream in(file);
	tractoare.clear();
	string linie, denumire, tip;
	int id, nr;
	while (getline(in, linie))
	{
		size_t poz = linie.find_first_of(",");
		id = stoi(linie.substr(0, poz));
		linie = linie.substr(poz + 1, linie.length());

		poz = linie.find_first_of(",");
		denumire = linie.substr(0, poz);
		linie = linie.substr(poz + 1, linie.length());

		poz = linie.find_first_of(",");
		tip = linie.substr(0, poz);
		linie = linie.substr(poz + 1, linie.length());

		nr = stoi(linie);

		Tractor t(id, denumire, tip, nr);
		tractoare.push_back(t);
	}
	in.close();
}


void RepoTractoare::incarcaInFisier()
{
	ofstream out(file);
	for (auto t : tractoare)
	{
		out << t.toString() << endl;
	}
	out.close();
}


vector<Tractor> RepoTractoare::getAll()
{
	return tractoare;
}

void RepoTractoare::adauga(Tractor t)
{
	string err = "";
	for (auto each : tractoare)
	{
		if (each.getId() == t.getId())
		{
			err = "Acelasi id";
		}
	}
	if (err.length() > 0)
	{
		throw(err);
	}
	tractoare.push_back(t);
	incarcaInFisier();
}


void RepoTractoare::modifica(int id, int roti)
{
	for (auto& t : tractoare)
	{
		if (t.getId() == id)
		{
			t.setRoti(roti);
			break;
		}
	}
	incarcaInFisier();
}