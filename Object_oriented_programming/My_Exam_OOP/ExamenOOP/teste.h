#pragma once
#include "service.h"

void testRepo()
{

	string f = "fisierTest.txt";
	ofstream out(f);
	out.close();

	RepoMelodii repo{ "fisierTest.txt" };

	Melodie m{ 1,"dsad", "ga", "pop" };

	assert(m.toString() == "1,dsad,ga,pop");

	repo.adauga(m);
	
	Melodie m2{ 2,"gdsf", "ab", "rock" };

	repo.adauga(m2);

	Melodie m3{ 3,"hbgd", "ce", "disco" };

	repo.adauga(m3);

	try
	{
		repo.adauga(m3);
		assert(false);
	}
	catch (int x)
	{
		assert(true);
	}


	auto melodii  = repo.getAll();
	assert(melodii.size() == 3);
	assert(melodii.at(0).getId() == 1);
	assert(melodii.at(0).getTitlu() == "dsad");
	assert(melodii.at(0).getArtist() == "ga");
	assert(melodii.at(0).getGen() == "pop");

	assert(melodii.at(1).getId() == 2);
	assert(melodii.at(1).getTitlu() == "gdsf");
	assert(melodii.at(1).getArtist() == "ab");
	assert(melodii.at(1).getGen() == "rock");


	assert(melodii.at(2).getId() == 3);
	assert(melodii.at(2).getTitlu() == "hbgd");
	assert(melodii.at(2).getArtist() == "ce");
	assert(melodii.at(2).getGen() == "disco");


	repo.sterge(2);

	melodii = repo.getAll();

	assert(melodii.size() == 2);
	assert(melodii.at(0).getId() == 1);
	assert(melodii.at(0).getTitlu() == "dsad");
	assert(melodii.at(0).getArtist() == "ga");
	assert(melodii.at(0).getGen() == "pop");

	assert(melodii.at(1).getId() == 3);
	assert(melodii.at(1).getTitlu() == "hbgd");
	assert(melodii.at(1).getArtist() == "ce");
	assert(melodii.at(1).getGen() == "disco");
}



void testService()
{
	string f = "fisierTest.txt";
	ofstream out(f);
	out.close();

	RepoMelodii repo{ "fisierTest.txt" };
	SrvMelodii srv{ repo };

	srv.adauga("aaa", "g", "pop");
	srv.adauga("bbb", "a", "folk");
	srv.adauga("ccc", "b", "disco");

	auto melodii = srv.getAll();

	assert(melodii.size() == 3);
	assert(melodii.at(0).getTitlu() == "bbb");
	assert(melodii.at(0).getArtist() == "a");
	assert(melodii.at(0).getGen() == "folk");

	assert(melodii.at(1).getTitlu() == "ccc");
	assert(melodii.at(1).getArtist() == "b");
	assert(melodii.at(1).getGen() == "disco");

	assert(melodii.at(2).getTitlu() == "aaa");
	assert(melodii.at(2).getArtist() == "g");
	assert(melodii.at(2).getGen() == "pop");

	int id = melodii.at(0).getId();
	srv.sterge(id);

	melodii = srv.getAll();

	assert(melodii.size() == 2);

	assert(melodii.at(0).getTitlu() == "ccc");
	assert(melodii.at(0).getArtist() == "b");
	assert(melodii.at(0).getGen() == "disco");

	assert(melodii.at(1).getTitlu() == "aaa");
	assert(melodii.at(1).getArtist() == "g");
	assert(melodii.at(1).getGen() == "pop");
}




void testAll()
{
	testRepo();
	testService();
}