#include "repository.h"

void RepoMelodii::citesteDinFisier()
{
	ifstream in(file);
	melodii.clear();
	string linie, titlu, artist, gen;
	int id;
	while (getline(in, linie))
	{
		size_t poz = linie.find_first_of(",");
		id = stoi(linie.substr(0, poz));
		linie = linie.substr(poz + 1, linie.length());

		poz = linie.find_first_of(",");
		titlu = linie.substr(0, poz);
		linie = linie.substr(poz + 1, linie.length());

		poz = linie.find_first_of(",");
		artist = linie.substr(0, poz);
		linie = linie.substr(poz + 1, linie.length());

		gen = linie;

		Melodie m{ id, titlu, artist, gen };
		melodii.push_back(m);
	}
	in.close();
}

void  RepoMelodii::incaracaInFisier()
{
	ofstream out(file);
	for (auto m : melodii)
	{
		out << m.toString() << endl;
	}
	out.close();
}

vector<Melodie>  RepoMelodii::getAll()
{
	return melodii;
}

void RepoMelodii::adauga(Melodie m)
{
	for (auto each : melodii)
	{
		if (each.getId() == m.getId())
		{
			throw(1);
		}
	}
	melodii.push_back(m);
	incaracaInFisier();
}


void RepoMelodii::sterge(int id)
{
	auto it = melodii.begin();
	for (it = melodii.begin(); it != melodii.end(); ++it)
	{
		Melodie m = *it;
		if (m.getId() == id)
		{
			break;
		}
	}
	melodii.erase(it);
	incaracaInFisier();
}