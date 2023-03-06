/********************************************************************************
** Form generated from reading UI file 'Testproduse.ui'
**
** Created by: Qt User Interface Compiler version 6.3.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_TESTPRODUSE_H
#define UI_TESTPRODUSE_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_TestproduseClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *TestproduseClass)
    {
        if (TestproduseClass->objectName().isEmpty())
            TestproduseClass->setObjectName(QString::fromUtf8("TestproduseClass"));
        TestproduseClass->resize(600, 400);
        menuBar = new QMenuBar(TestproduseClass);
        menuBar->setObjectName(QString::fromUtf8("menuBar"));
        TestproduseClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(TestproduseClass);
        mainToolBar->setObjectName(QString::fromUtf8("mainToolBar"));
        TestproduseClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(TestproduseClass);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        TestproduseClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(TestproduseClass);
        statusBar->setObjectName(QString::fromUtf8("statusBar"));
        TestproduseClass->setStatusBar(statusBar);

        retranslateUi(TestproduseClass);

        QMetaObject::connectSlotsByName(TestproduseClass);
    } // setupUi

    void retranslateUi(QMainWindow *TestproduseClass)
    {
        TestproduseClass->setWindowTitle(QCoreApplication::translate("TestproduseClass", "Testproduse", nullptr));
    } // retranslateUi

};

namespace Ui {
    class TestproduseClass: public Ui_TestproduseClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_TESTPRODUSE_H
