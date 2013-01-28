#include "uml_mode.h"
#include "uml_scene.h"

UMLMode::UMLMode()
	: delegateScene( NULL )
{

}

UMLMode::UMLMode( UMLScene * delegateScene )
	: delegateScene( delegateScene )
{

}

void UMLMode::onMousePress( QGraphicsSceneMouseEvent * event )
{

}

void UMLMode::onMouseMove( QGraphicsSceneMouseEvent * event )
{

}

void UMLMode::onMouseRelease( QGraphicsSceneMouseEvent * event )
{

}
