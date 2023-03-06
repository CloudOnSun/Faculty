#include "service.h"
#include <exception>
vector<Melodie> SrvMelodii::getAll()
{
	return repo.getAll();
}

void SrvMelodii::update(int id, string titlu, int rank)
{
	repo.update(id, titlu, rank);
}

void SrvMelodii::sterge(int id, string artist)
{
	int nr = 0;
	auto melodii = repo.getAll();
	for (auto m : melodii)
	{
		if (m.getArtist() == artist)
			nr++;
	}
	if (nr == 1)
		throw (nr);
	else
		repo.sterge(id);
}