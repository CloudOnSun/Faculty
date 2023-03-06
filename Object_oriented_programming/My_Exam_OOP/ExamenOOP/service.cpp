#include "service.h"
#include <random>

vector<Melodie> SrvMelodii::getAll()
{
	auto v = repo.getAll();

	sort(v.begin(), v.end(), [&](Melodie m1, Melodie m2) { return m1.getArtist() < m2.getArtist(); });

	return v;
}

void SrvMelodii::adauga(string titlu, string artist, string gen)
{
	bool gata = false;

	while (!gata)
	{
		try
		{
			int id = rand();
			Melodie m{ id, titlu, artist, gen };
			repo.adauga(m);
			gata = true;
		}
		catch (int x)
		{
			continue;
		}
	}
}


void SrvMelodii::sterge(int id)
{
	repo.sterge(id);
}