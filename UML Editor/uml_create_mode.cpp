#include "uml_create_mode.h"

CreateMode::CreateMode()
	: _umlObjectType( UMLObject::Class )
{

}

CreateMode::CreateMode( UMLScene * delegateScene, UMLObject::Type objectType )
	: UMLMode( delegateScene )
	, _umlObjectType( objectType )
{

}

void CreateMode::onMousePress( QGraphicsSceneMouseEvent * event )
{
	UMLObject * umlObject;
	switch ( _umlObjectType )
	{
		case UMLObject::Class:
			umlObject = new UMLClass();
			break;
		case UMLObject::UseCase:
			umlObject = new UMLUseCase();
			break;
		default:
			break;
	}
	
	umlObject->setPos( event->scenePos() );
	this->delegateScene->addItem( umlObject );
	this->delegateScene->umlObjectList.push_back( umlObject );
}
