#pragma once
#include "repository.h"

class SrvMelodii
{
private:
	
	RepoMelodii& repo;

public:

	SrvMelodii(RepoMelodii& repo) : repo{repo} {}

	vector<Melodie> getAll();

	void update(int id, string titlu, int rank);

	void sterge(int id, string artist);
};