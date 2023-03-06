#include "mainActivityWidget.h"
#include <qmessagebox.h>
#include <qlabel.h>


MainActivityWidget::MainActivityWidget(QWidget* parent) : QWidget(parent)
{
	//this->listaFilmeWidget = new QListWidget;
	this->listModel = new MyListModel(service.getToateFilmele());
	this->listView = new QListView;
	listView->setModel(listModel);
	this->tabelFilme = new QTableWidget(0, 4);
	
	this->butonCosCrud = new QPushButton("CosCRUD");
	this->butonCosPaint = new QPushButton("CosPaint");
	
	QString labelTitlu("Titlu");
	QString labelGen("Gen");
	QString labelAn("An aparitie");
	QString labelActor("Actor principal");
	QStringList listaHeadere{ labelTitlu, labelGen, labelAn, labelActor };
	this->tabelFilme->setHorizontalHeaderLabels(listaHeadere);
	this->basicOperationsWidget = new BasicOperationsWidget(service);
	this->getSortFiltruWidget = new GetSortFiltruWidget(service);
	this->contorCos = new ContorCosCumparaturi(service);
	this->listaCos = new ListaCosCumparaturi(service);
	this->cosCumparaturiWidget = new CosCumparaturiWidget(service);
	/*this->cosCRUD = new CosCRUDGUI(service);
	cosCRUD->adaugaObservator(contorCos);
	cosCRUD->adaugaObservator(listaCos);
	cosCumparaturiWidget->adaugaObservator(cosCRUD);*/
	cosCumparaturiWidget->adaugaObservator(contorCos);
	cosCumparaturiWidget->adaugaObservator(listaCos);

	

	QVBoxLayout* verticalLayout = new QVBoxLayout();
	verticalLayout->addWidget(basicOperationsWidget);
	verticalLayout->addWidget(getSortFiltruWidget);

	QHBoxLayout* butoaneGenuriLayout = new QHBoxLayout();
	adaugaButoaneGenuri(butoaneGenuriLayout);
	QWidget* genuriWidget = new QWidget;
	genuriWidget->setLayout(butoaneGenuriLayout);

	verticalLayout->addWidget(genuriWidget);

	QWidget* operatiiFilmWidget = new QWidget;
	operatiiFilmWidget->setLayout(verticalLayout);


	QHBoxLayout* horizontalLayout = new QHBoxLayout();
	horizontalLayout->addWidget(listView);
	horizontalLayout->addWidget(tabelFilme);
	horizontalLayout->addWidget(operatiiFilmWidget);
	QWidget* filmWidget = new QWidget;
	filmWidget->setLayout(horizontalLayout);

	QVBoxLayout* operatiiCosLayout = new QVBoxLayout();
	operatiiCosLayout->addWidget(contorCos);
	operatiiCosLayout->addWidget(cosCumparaturiWidget);
	operatiiCosLayout->addWidget(butonCosCrud);
	operatiiCosLayout->addWidget(butonCosPaint);
	QWidget* operatiiCosWidget = new QWidget;
	operatiiCosWidget->setLayout(operatiiCosLayout);

	QHBoxLayout* cosCumparaturiLayout = new QHBoxLayout();
	cosCumparaturiLayout->addWidget(listaCos);
	cosCumparaturiLayout->addWidget(operatiiCosWidget);
	QWidget* cosWidget = new QWidget;
	cosWidget->setLayout(cosCumparaturiLayout);

	QVBoxLayout* mainWindowLayout = new QVBoxLayout;
	mainWindowLayout->addWidget(filmWidget);
	mainWindowLayout->addWidget(cosWidget);

	this->setLayout(mainWindowLayout);

	this->creeazaConexiuni();
	this->updateCasutaFilme(service.getToateFilmele());
}


void MainActivityWidget::deschidCosCRUD()
{
	CosCRUDGUI* cosCRUD = new CosCRUDGUI(service);
	cosCRUD->adaugaObservator(contorCos);
	cosCRUD->adaugaObservator(listaCos);
	for (auto element : listaCosuriCRUD)
	{
		cosCRUD->adaugaObservator(element);
		element->adaugaObservator(cosCRUD);
	}
	for (auto observator : observatori)
	{
		cosCRUD->adaugaObservator(observator);
	}
	cosCumparaturiWidget->adaugaObservator(cosCRUD);
	listaCosuriCRUD.push_back(cosCRUD);
	cosCRUD->show();
}


void MainActivityWidget::deschidCosReadOnlyGUI()
{
	CosReadOnlyGUI* cosPaint = new CosReadOnlyGUI(service);
	cosCumparaturiWidget->adaugaObservator(cosPaint);
	for (auto element : listaCosuriCRUD)
	{
		element->adaugaObservator(cosPaint);
	}
	observatori.push_back(cosPaint);
	cosPaint->show();
}


void MainActivityWidget::conecteazaListaFilmeCuInputFilme()
{
	QObject::connect(this->listView->selectionModel(), &QItemSelectionModel::selectionChanged, [this]() {

		if (this->listView->selectionModel()->selectedIndexes().isEmpty())
		{
			this->basicOperationsWidget->inputFilmeWidget->getLinieTitlu()->setText("");
			this->basicOperationsWidget->inputFilmeWidget->getLinieGen()->setText("");
			this->basicOperationsWidget->inputFilmeWidget->getLinieAnAparitie()->setValue(0);
			this->basicOperationsWidget->inputFilmeWidget->getLinieActorPrincipal()->setText("");
			return;
		}
		auto selectedIndex = this->listView->selectionModel()->selectedIndexes().at(0);
		QString item = selectedIndex.data(Qt::DisplayRole).toString();
		QList filmSeparat = item.split(',');
		this->basicOperationsWidget->inputFilmeWidget->getLinieTitlu()->setText(filmSeparat.at(0));
		this->basicOperationsWidget->inputFilmeWidget->getLinieGen()->setText(filmSeparat.at(1));
		this->basicOperationsWidget->inputFilmeWidget->getLinieAnAparitie()->setValue(filmSeparat.at(2).toInt());
		this->basicOperationsWidget->inputFilmeWidget->getLinieActorPrincipal()->setText(filmSeparat.at(3));
		return;
	});
}


void MainActivityWidget::creeazaConexiuni()
{
	QWidget::connect(basicOperationsWidget, &BasicOperationsWidget::actiuneCuSucces, [&]() {
			
		QString mesaj{ "Actiune efectuata cu succes" };
		QMessageBox::information(this, "Actiune", mesaj);

	});

	QWidget::connect(getSortFiltruWidget, &GetSortFiltruWidget::butonApasat, [&](const vector<Film>& lista_filme) {

		updateCasutaFilme(lista_filme);

	});

	QWidget::connect(butonCosCrud, &QPushButton::clicked, [&]() {this->deschidCosCRUD(); });

	QWidget::connect(butonCosPaint, &QPushButton::clicked, [&]() {this->deschidCosReadOnlyGUI(); });

	this->conecteazaListaFilmeCuInputFilme();
}


void MainActivityWidget::updateCasutaFilme(const vector<Film>& listaFilme)
{
	/*this->listaFilmeWidget->clear();

	for (auto const& film : listaFilme)
	{
		QListWidgetItem* item = new QListWidgetItem(QString::fromStdString(film.toString()));
		this->listaFilmeWidget->addItem(item);
	}*/

	listModel->setFilme(listaFilme);

	this->tabelFilme->setRowCount(0);

	int numarLini = 0;
	for (auto const& film : listaFilme)
	{
		this->tabelFilme->insertRow(numarLini);
		tabelFilme->setItem(numarLini, 0, new QTableWidgetItem(QString::fromStdString(film.get_titlu())));
		tabelFilme->setItem(numarLini, 1, new QTableWidgetItem(QString::fromStdString(film.get_gen())));
		tabelFilme->setItem(numarLini, 2, new QTableWidgetItem(QString::fromStdString(std::to_string(film.get_an_aparitie()))));
		tabelFilme->setItem(numarLini, 3, new QTableWidgetItem(QString::fromStdString(film.get_actorPrincipal())));
		numarLini++;
	}
}


void MainActivityWidget::adaugaButoaneGenuri(QLayout* layout)
{
	auto& listaFilme = service.getToateFilmele();
	vector<string> genuri;
	for (auto& film : listaFilme)
	{
		string gen = film.get_gen();

		vector<string>::const_iterator pointerFilm = std::find_if(genuri.begin(), genuri.end(), [=](string genFilm) { return genFilm == gen; });

		if (pointerFilm != genuri.end())
		{
			continue;
		}
		else
		{
			genuri.push_back(gen);
			QPushButton* butonGen = new QPushButton(QString::fromStdString(gen));
			QWidget::connect(butonGen, &QPushButton::clicked, [this,butonGen]() {this->getNumarFilmeDupaGen(butonGen); });
			layout->addWidget(butonGen);
		}
	}
}


void MainActivityWidget::getNumarFilmeDupaGen(QPushButton* butonGen)
{
	string gen = butonGen->text().toStdString();
	auto& listaFilme = service.getToateFilmele();
	int numarFilme = 0;
	for (auto& film : listaFilme)
	{
		if (film.get_gen() == gen)
		{
			numarFilme++;
		}
	}
	QMessageBox::information(this, "Numar filme", QString::fromStdString(std::to_string(numarFilme)));
}