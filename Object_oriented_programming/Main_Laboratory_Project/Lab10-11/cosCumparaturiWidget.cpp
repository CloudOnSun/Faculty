#include "cosCumparaturiWidget.h"
#include "qformlayout.h"
#include "qboxlayout.h"
#include "qmessagebox.h"
#include <qpainter.h>

ContorCosCumparaturi::ContorCosCumparaturi(Service& service): CosCumparaturiObservator(), service{service}
{
	this->contor = new QLineEdit;
	contor->setReadOnly(true);
	QFormLayout* formLayout = new QFormLayout;
	formLayout->addRow("Filme in cos: ", contor);
	this->setLayout(formLayout);
}


void ContorCosCumparaturi::update()
{
	string lungimeCos = std::to_string(service.getLungimeCosCumparaturi());
	
	contor->setText(QString::fromStdString(lungimeCos));
}


ListaCosCumparaturi::ListaCosCumparaturi(Service& service) : CosCumparaturiObservator(), service{ service }
{
	this->listaCos = new QListWidget;
	QHBoxLayout* horizontalLayout = new QHBoxLayout;
	horizontalLayout->addWidget(listaCos);
	this->setLayout(horizontalLayout);
	this->update();
}


void ListaCosCumparaturi::update()
{
	const auto& listaFilme = service.getCosCumparaturi();

	this->listaCos->clear();

	for (const auto& film : listaFilme)
	{
		QListWidgetItem* item = new QListWidgetItem(QString::fromStdString(film.toString()));
		this->listaCos->addItem(item);
	}
}


CosCumparaturiWidget::CosCumparaturiWidget(Service& service, QWidget* parent) : QWidget(parent), service{ service }
{
	this->adaugaInCos = new QPushButton("Adauga in cos");
	this->golesteCos = new QPushButton("Goleste cosul");
	this->adaugaRandom = new QPushButton("Adauga filme random");
	this->exportInFisier = new QPushButton("Export in fisier");
	
	QFormLayout* formLayout = new QFormLayout;
	formLayout->addRow(adaugaInCos);
	formLayout->addRow(golesteCos);
	formLayout->addRow(adaugaRandom);
	formLayout->addRow(exportInFisier);

	this->setLayout(formLayout);
	
	creeazaConexiuni();
}


void CosCumparaturiWidget::creeazaConexiuni()
{
	QWidget::connect(adaugaInCos, &QPushButton::clicked, [&]() { this->butonAdaugaApasat(); });

	QWidget::connect(golesteCos, &QPushButton::clicked, [&]() { this->butonGolesteApasat(); });

	QWidget::connect(adaugaRandom, &QPushButton::clicked, [&]() { this->butonRandomApasat(); });

	QWidget::connect(exportInFisier, &QPushButton::clicked, [&]() { this->butonExportApasat(); });

}


void CosCumparaturiWidget::butonAdaugaApasat()
{
	QString titlu{ "titlu" };
	TextFilmWidget* getTitlu = new TextFilmWidget(titlu);
	getTitlu->show();
	QWidget::connect(getTitlu, &TextFilmWidget::textIntrodus, [&](QString* titlu) {

		try
		{
			service.adaugaFilmInCosCumparaturi((*titlu).toStdString());
			notificaObservatorii();
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


void CosCumparaturiWidget::butonGolesteApasat()
{
	try
	{
		service.golesteCosCumparaturi();
		notificaObservatorii();
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


void CosCumparaturiWidget::butonRandomApasat()
{
	QString titlu{ "Numar de filme: " };
	int capacitate = (int)service.getToateFilmele().size();
	NumarFilmWidget* getNumar = new NumarFilmWidget(titlu, 0, capacitate);
	getNumar->show();
	QWidget::connect(getNumar, &NumarFilmWidget::numarIntrodus, [&](int* numar) {
		try
		{
			service.adaugaFilmeRandomInCos((*numar));
			notificaObservatorii();
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


void CosCumparaturiWidget::butonExportApasat()
{
	QString titlu{ "Nume fisier: " };
	TextFilmWidget* getTitlu = new TextFilmWidget(titlu);
	getTitlu->show();
	QWidget::connect(getTitlu, &TextFilmWidget::textIntrodus, [&](QString* titlu) {

		try
		{
			service.exportFisierCosCumparaturi((*titlu).toStdString());
			notificaObservatorii();
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


CosCRUDGUI::CosCRUDGUI(Service& service) : CosCumparaturiObservator()
{
	operatiiCos = new CosCumparaturiWidget(service);
	listaCos = new ListaCosCumparaturi(service);
	operatiiCos->adaugaObservator(listaCos);

	QHBoxLayout* layout = new QHBoxLayout;
	layout->addWidget(listaCos);
	layout->addWidget(operatiiCos);
	this->setLayout(layout);
}

void CosCRUDGUI::update()
{
	listaCos->update();
}


void CosReadOnlyGUI::update() 
{
	this->repaint();
}


void CosReadOnlyGUI::paintEvent(QPaintEvent* ) 
{
	QPainter painter{ this };
	auto listaCos = service.getCosCumparaturi();

	//int lungime = int(service.getLungimeCosCumparaturi());

	int i = 1;

	for (auto film : listaCos)
	{
		painter.drawLine(20*i, 20*i, width(), 20*i);
		i++;
	}
}


CosReadOnlyGUI::CosReadOnlyGUI(Service& service) : CosCumparaturiObservator(), service{service}
{

}

