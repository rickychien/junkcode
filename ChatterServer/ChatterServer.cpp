#include "ChatterServer.h"
#include <QRegExp>

ChatterServer::ChatterServer( QObject * parent )
	: QTcpServer( parent )
{
	bool success = this->listen( QHostAddress::Any, 4200 );

    if( !success ) {
        qFatal( "Could not listen on port 4200." );
    }
	else {
		qDebug() << "Ready";
	}
}

ChatterServer::~ChatterServer()
{
	foreach( QTcpSocket * client, clients ) {
		delete client;
	}
}

void ChatterServer::incomingConnection( int socketDescriptor )
{
    QTcpSocket * client = new QTcpSocket( this );
    client->setSocketDescriptor( socketDescriptor );
    clients.insert( client );

    qDebug() << "New client from:" << client->peerAddress().toString();

    connect( client, SIGNAL( readyRead() ), this, SLOT( onReadyRead() ) );
    connect( client, SIGNAL( disconnected() ), this, SLOT( onDisconnected() ) );
	connect( client, SIGNAL( error( QAbstractSocket::SocketError ) ), 
			 this, SLOT( onError( QAbstractSocket::SocketError ) ) );
}

void ChatterServer::onReadyRead()
{
    QTcpSocket * client = ( QTcpSocket * )sender();
    while( client->canReadLine() ) {
        QString line = QString::fromUtf8( client->readLine() ).trimmed();
        qDebug() << "Read line:" << line;

        QRegExp meRegex( "^/me:(.*)$" );

        if( meRegex.indexIn( line ) != -1 ) {
            QString user = meRegex.cap( 1 );
            users[ client ] = user;

            foreach( QTcpSocket * client, clients ) {
                client->write( QString( "Server:" + user + " has joined.\n" ).toUtf8() );
			}
            sendUserList();
        }
        else if( users.contains( client ) ) {
            QString message = line;
            QString user = users[ client ];
            qDebug() << "User:" << user;
            qDebug() << "Message:" << message;

            foreach( QTcpSocket * otherClient, clients ) {
                otherClient->write( QString( user + ":" + message + "\n" ).toUtf8() );
			}
        }
        else {
            qWarning() << "Got bad message from client:" << client->peerAddress().toString() << line;
        }
    }
}

void ChatterServer::onDisconnected()
{
    QTcpSocket * client = ( QTcpSocket * )sender();
    qDebug() << "Client disconnected:" << client->peerAddress().toString();

    clients.remove( client );

    QString user = users[ client ];
    users.remove( client );

    sendUserList();
    foreach( QTcpSocket * client, clients )
        client->write( QString( "Server:" + user + " has left.\n" ).toUtf8() );
}

void ChatterServer::sendUserList()
{
    QStringList userList;
    foreach( QString user, users.values() )
        userList << user;

    foreach( QTcpSocket * client, clients )
        client->write( QString( "/users:" + userList.join( "," ) + "\n" ).toUtf8() );
}

void ChatterServer::onError( QAbstractSocket::SocketError socketError )
{
	QTcpSocket * client = ( QTcpSocket * )sender();
	qDebug() << client->errorString();
}