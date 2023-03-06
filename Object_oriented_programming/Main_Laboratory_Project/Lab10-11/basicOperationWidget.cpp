#include "basicOperationsWidget.h"
#include <qformlayout.h>
#include <qmessagebox.h>

/*TextFilmWidget::TextFilmWidget(QString& informatieDorita, QWidget* parent) : QWidget(parent), informatieDorita{informatieDorita}
{
	this->linieText = new QLineEdit;
	this->butonOK = new QPushButton("OK");
	QFormLayout* formLayout = new QFormLayout;
	formLayout->addRow(informatieDorita, linieText);
	formLayout->addWidget(butonOK);
	this->setLayout(formLayout);
	this->conecteazaActiune();
}


/*void TextFilmWidget::verificaTitluFilm()
{
	try
	{
		string titlu = this->linieTitlu->text().toStdString();
		service.cautaFilm(titlu);
		emit titluValid();
	}
	catch (ExceptieRepository& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}
}--

void TextFilmWidget::executaActiune()
{
	rezultat = new QString(linieText->text());
	emit textIntrodus(rezultat);
	this->close();
}

void TextFilmWidget::conecteazaActiune()
{
	QWidget::connect(this->butonOK, &QPushButton::clicked, [&]() { this->executaActiune(); });

}*/


////////////////////////////////////////////////////////////////////////////////////////////


ModificaWidget::ModificaWidget(Service& service, QWidget* parent): QWidget(parent), service{service}
{
	this->linieGenNou = new QLineEdit;
	this->linieAnAparitieNou = new QSpinBox;
	linieAnAparitieNou->setMinimum(1900);
	linieAnAparitieNou->setMaximum(2022);
	this->linieActorPrincialNou = new QLineEdit;
	this->modificaFilmButon = new QPushButton("Modifica");

	QFormLayout* formLayout = new QFormLayout;
	formLayout->addRow("Noul Gen", linieGenNou);
	formLayout->addRow("Noul An", linieAnAparitieNou);
	formLayout->addRow("Noul Actor", linieActorPrincialNou);
	formLayout->addWidget(modificaFilmButon);
	this->setLayout(formLayout);
	this->creeazaConexiuni();
}


void ModificaWidget::butonModificaApasat()
{
	string tiitluFilmSTD = this->titluFilm.toStdString();
	string genFilm = this->linieGenNou->text().toStdString();
	int anFilm = this->linieAnAparitieNou->value();
	string actorFilm = this->linieActorPrincialNou->text().toStdString();

	try
	{
		service.modificaFilm(tiitluFilmSTD, genFilm, anFilm, actorFilm);
		emit modificareCuSucces();
		this->close();
	}
	catch(ExceptieRepository& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}
	catch (ExceptieValidare& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}
	catch (ExceptieRepositoryProabilitate& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}

}


void ModificaWidget::creeazaConexiuni()
{
	QWidget::connect(this->modificaFilmButon, &QPushButton::clicked, [&]() { this->butonModificaApasat(); });
}


////////////////////////////////////////////////////////////////////////////////////////////


BasicOperationsWidget::BasicOperationsWidget(Service& service, QWidget* parent): QWidget(parent), service{service}
{
	this->adaugaButon = new QPushButton("Adauga");
	this->stergeButon = new QPushButton("Sterge");
	this->modificaButon = new QPushButton("Modifica");
	this->cautaButon = new QPushButton("Cauta");
	this->undoButon = new QPushButton("Undo");
	this->inputFilmeWidget = new InputFilmeWidget();
	this->modificaWidget = new ModificaWidget(service);

	QVBoxLayout* verticalLayout = new QVBoxLayout;
	QHBoxLayout* horizontalLayout = new QHBoxLayout;

	verticalLayout->addWidget(inputFilmeWidget);

	horizontalLayout->addWidget(adaugaButon);
	horizontalLayout->addWidget(stergeButon);
	horizontalLayout->addWidget(modificaButon);
	horizontalLayout->addWidget(cautaButon);
	horizontalLayout->addWidget(undoButon);

	QWidget* details = new QWidget();
	details->setLayout(horizontalLayout);

	verticalLayout->addWidget(details);

	this->setLayout(verticalLayout);

	this->creeazaConexiuni();

}


void BasicOperationsWidget::butonAdaugaApasat()
{
	string titluFilm = inputFilmeWidget->getLinieTitlu()->text().toStdString();
	string genFilm = inputFilmeWidget->getLinieGen()->text().toStdString();
	int anFilm = inputFilmeWidget->getLinieAnAparitie()->value();
	string actorFilm = inputFilmeWidget->getLinieActorPrincipal()->text().toStdString();

	try
	{
		service.adaugaFilm(titluFilm, genFilm, anFilm, actorFilm);
		emit actiuneCuSucces();
	}
	catch (ExceptieRepository& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}
	catch (ExceptieValidare& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}
	catch (ExceptieRepositoryProabilitate& exceptie)
	{
		QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
		return;
	}
}


void BasicOperationsWidget::butonStergeApasat()
{
	QString informatie = "Titlu";
	TextFilmWidget* getTitlu = new TextFilmWidget(informatie);
	getTitlu->show();
	QWidget::connect(getTitlu, &TextFilmWidget::textIntrodus, [&](QString* titlu) {
		try
		{
			service.stergeFilm((*titlu).toStdString());
			emit actiuneCuSucces();
		}
		catch (ExceptieRepository& exceptie)
		{
			QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
			return;
		}
		catch (ExceptieValidare& exceptie)
		{
			QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
			return;
		}
		catch (ExceptieRepositoryProabilitate& exceptie)
		{
			QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
			return;
		}
	});

}


void BasicOperationsWidget::butonModificaApasat()
{

	QString informatie = "Titlu";
	TextFilmWidget* getTitlu = new TextFilmWidget(informatie);
	getTitlu->show();
	QWidget::connect(getTitlu, &TextFilmWidget::textIntrodus, [&](QString* titlu) {
	
		modificaWidget->setTitluFilm((*titlu));
		modificaWidget->show();
		
	});
}


void BasicOperationsWidget::butonCautaApasat()
{
	QString informatie = "Titlu";
	TextFilmWidget* getTitlu = new TextFilmWidget(informatie);
	getTitlu->show();
	QWidget::connect(getTitlu, &TextFilmWidget::textIntrodus, [&](QString* titlu) {
		try
		{
			auto const& film = service.cautaFilm((*titlu).toStdString());
			QMessageBox::information(this, "Film", QString::fromStdString(film.toString()));
		}
		catch (ExceptieRepository& exceptie)
		{
			QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
			return;
		}
		catch (ExceptieValidare& exceptie)
		{
			QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
			return;
		}
		catch (ExceptieRepositoryProabilitate& exceptie)
		{
			QMessageBox::warning(this, "Error", QString::fromStdString(exceptie.get_mesaj()));
			return;
		}
	});
}


void BasicOperationsWidget::butonUndoApasat()
{
	service.undo();
	emit actiuneCuSucces();
}


void BasicOperationsWidget::creeazaConexiuni()
{
	QWidget::connect(adaugaButon, &QPushButton::clicked, [&]() { this->butonAdaugaApasat(); });

	QWidget::connect(stergeButon, &QPushButton::clicked, [&]() { this->butonStergeApasat(); });

	QWidget::connect(modificaButon, &QPushButton::clicked, [&]() { this->butonModificaApasat(); });

	QWidget::connect(cautaButon, &QPushButton::clicked, [&]() { this->butonCautaApasat(); });

	QWidget::connect(modificaWidget, &ModificaWidget::modificareCuSucces, [&]() {emit actiuneCuSucces(); });

	QWidget::connect(undoButon, &QPushButton::clicked, [&]() { this->butonUndoApasat(); });
}
