#include "repository.h"
#include <fstream>

void RepoMelodii::citesteDinFisier()
{
	ifstream in(file);
	melodii.clear();
	string linie;
	string titlu, artist;
	int id, rank;
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

		poz = linie.find_first_of(",");
		rank = stoi(linie.substr(0, poz));
		linie = linie.substr(poz + 1, linie.length());

		Melodie m{ id, titlu, artist, rank };
		melodii.push_back(m);
	}
	in.close();
}


void RepoMelodii::incarcaInFisier()
{
	ofstream out(file);
	for (auto m : melodii)
	{
		out << m.toString() + "\n";
	}
	out.close();
}


vector<Melodie> RepoMelodii::getAll()
{
	return melodii;
}

void RepoMelodii::update(int id, string titlu, int rank)
{
	int i = -1;
	for (auto& m : melodii)
	{
		i++;
		if (m.getId() == id)
		{
			break;
		}
	}
	melodii.at(i).setRank(rank);
	melodii.at(i).setTitlu(titlu);
	incarcaInFisier();
}

void RepoMelodii::sterge(int id)
{
	for (auto it = melodii.begin(); it != melodii.end(); ++it)
	{
		auto m = *it;
		if (m.getId() == id)
		{
			melodii.erase(it);
			break;
		}
	}
	incarcaInFisier();
}