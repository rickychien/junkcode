#include "ChatterClient.h"
#include <QRegExp>
#include <QMessageBox>

ChatterClient::ChatterClient( QWidget * parent, Qt::WFlags flags )
	: QMainWindow( parent, flags )
{
	ui.setupUi( this );
	
	// Make sure that we are showing the login page when we startup
    ui.stackedWidget->setCurrentWidget( ui.loginPage );

	socket = new QTcpSocket( this );

	connect( socket, SIGNAL( readyRead() ), this, SLOT( onReadyRead() ) );
    connect( socket, SIGNAL( connected() ), this, SLOT( onConnected() ) );
	connect( socket, SIGNAL( disconnected() ), this, SLOT( onDisconnected() ) );
	connect( socket, SIGNAL( error( QAbstractSocket::SocketError ) ), 
			 this, SLOT( onError( QAbstractSocket::SocketError ) ) );
}

ChatterClient::~ChatterClient()
{
	delete socket;
}

void ChatterClient::on_loginButton_clicked()
{
	socket->connectToHost( ui.serverLineEdit->text(), 4200 );
}

void ChatterClient::on_sayButton_clicked()
{
    // What did they want to say (minus white space around the string):
    QString message = ui.sayLineEdit->text().trimmed();

    // Only send the text to the chat server if it's not empty:
    if( !message.isEmpty() ) {
        socket->write( QString( message + "\n" ).toUtf8() );
    }

    // Clear out the input box so they can type something else:
    ui.sayLineEdit->clear();

    // Put the focus back into the input box so they can type again:
    ui.sayLineEdit->setFocus();
}

void ChatterClient::onReadyRead()
{
    // We'll loop over every (complete) line of text that the server has sent us:
    while( socket->canReadLine() ) {
        // Here's the line the of text the server sent us (we use UTF-8 so
        // that non-English speakers can chat in their native language)
        QString line = QString::fromUtf8( socket->readLine() ).trimmed();

        // These two regular expressions describe the kinds of messages
        // the server can send us:

        // Normal messges look like this: "username:The message"
        QRegExp messageRegex( "^([^:]+):(.*)$" );

        // Any message that starts with "/users:" is the server sending us a
        // list of users so we can show that list in our GUI:
        QRegExp usersRegex( "^/users:(.*)$" );

        // Is this a users message:
        if( usersRegex.indexIn( line ) != -1 ) {
            // If so, udpate our users list on the right:
            QStringList users = usersRegex.cap( 1 ).split( "," );
            ui.userListWidget->clear();

            foreach( QString user, users ) {
                new QListWidgetItem( QPixmap( ":/user.png" ), user, ui.userListWidget );
			}
        }
        // Is this a normal chat message:
        else if( messageRegex.indexIn( line ) != -1 ) {
            // If so, append this message to our chat box:
            QString user = messageRegex.cap( 1 );
            QString message = messageRegex.cap( 2 );

            ui.roomTextEdit->append( "<b>" + user + "</b>: " + message );
        }
    }
}

void ChatterClient::onConnected()
{
    // Flip over to the chat page:
    ui.stackedWidget->setCurrentWidget( ui.chatPage );

    // And send our username to the chat server.
    socket->write( QString( "/me:" + ui.userLineEdit->text() + "\n" ).toUtf8() );
}

void ChatterClient::onError( QAbstractSocket::SocketError socketError )
{
	QMessageBox::information( NULL, "Network error", socket->errorString(),
							  QMessageBox::Yes, QMessageBox::Yes);
}