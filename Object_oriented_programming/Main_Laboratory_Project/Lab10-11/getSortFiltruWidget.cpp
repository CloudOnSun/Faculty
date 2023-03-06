#include "getSortFiltruWidget.h"
#include <qboxlayout.h>
#include <qmessagebox.h>

GetSortFiltruWidget::GetSortFiltruWidget(Service& service, QWidget* parent) : QWidget(parent), service{ service }
{
	this->getAllButon = new QPushButton("Toate Filmele");
	this->sorteazaButon = new QPushButton("Sorteaza");
	this->filtreazaButon = new QPushButton("Filtreaza");
	this->criteriuSortare = new QComboBox;
	criteriuSortare->addItem("titlu");
	criteriuSortare->addItem("gen");
	criteriuSortare->addItem("an aparitie");
	criteriuSortare->addItem("actor principal");
	this->criteriuFiltrare = new QComboBox;
	criteriuFiltrare->addItem("titlu");
	criteriuFiltrare->addItem("an aparitie");

	QHBoxLayout* horizontalLayout = new QHBoxLayout;
	
	horizontalLayout->addWidget(getAllButon);
	horizontalLayout->addWidget(sorteazaButon);
	horizontalLayout->addWidget(criteriuSortare);
	horizontalLayout->addWidget(filtreazaButon);
	horizontalLayout->addWidget(criteriuFiltrare);

	this->setLayout(horizontalLayout);

	this->creeazaConexiuni();
}


void GetSortFiltruWidget::creeazaConexiuni()
{
	QWidget::connect(getAllButon, &QPushButton::clicked, [&]() {this->butonGetAllApasat(); });

	QWidget::connect(sorteazaButon, &QPushButton::clicked, [&]() {this->butonSortareApasat(); });

	QWidget::connect(filtreazaButon, &QPushButton::clicked, [&]() {this->butonFiltrareApasat(); });
}


void GetSortFiltruWidget::butonGetAllApasat()
{
	try
	{
		const auto& lista_filme = service.getToateFilmele();
		emit butonApasat(lista_filme);
	}
	catch (ExceptieRepositoryProabilitate& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}
}


void GetSortFiltruWidget::butonSortareApasat()
{
	string criteriu = criteriuSortare->currentText().toStdString();
	try
	{
		const auto& lista_filme = service.sortareFilme(criteriu);
		emit butonApasat(lista_filme);
	}
	catch (ExceptieRepositoryProabilitate& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}
}


void GetSortFiltruWidget::butonFiltrareApasat()
{
	QString criteriu = criteriuFiltrare->currentText();
	if (criteriu == "titlu")
	{

		TextFilmWidget* getTitlu =  new TextFilmWidget(criteriu);
		getTitlu->show();
		QWidget::connect(getTitlu, &TextFilmWidget::textIntrodus, [&](QString* titlu) {

			try
			{
				const auto& lista_filme = service.filtrareFilmeTitlu((*titlu).toStdString());
				emit butonApasat(lista_filme);
			}
			catch (ExceptieRepositoryProabilitate& exceptie)
			{
				QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
				return;
			}

		});
	}
	else if (criteriu == "an aparitie")
	{
		NumarFilmWidget* getAn = new NumarFilmWidget(criteriu);
		getAn->show();
		QWidget::connect(getAn, &NumarFilmWidget::numarIntrodus, [&](int* anAparitie) {

			try
			{
				const auto& lista_filme = service.filtrareFilmeAnAparitie((*anAparitie));
				emit butonApasat(lista_filme);
			}
			catch (ExceptieRepositoryProabilitate& exceptie)
			{
				QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
				return;
			}

		});
	}
}