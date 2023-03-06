#pragma once
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

class Melodie
{
private:

	int id;
	string titlu;
	string artist;
	string gen;

public:

	Melodie(int id, string titlu, string artist, string gen) : id{id}, titlu{titlu}, artist{artist}, gen{gen} {}

	int getId()
	{
		return id;
	}

	string getTitlu()
	{
		return titlu;
	}

	string getArtist()
	{
		return artist;
	}

	string getGen()
	{
		return gen;
	}

	string toString()
	{
		return to_string(id) + "," + titlu + "," + artist + "," + gen;
	}
};