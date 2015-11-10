package com.gdx.shaw.game;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gdx.shaw.game.player.PlayerActor;
import com.gdx.shaw.game.screen.GameScreen;
import com.gdx.shaw.utils.Constants;
import com.gdx.shaw.utils.DeBug;

/**
 *	更新世界相机的位置
 */
public class UpdateCamera implements Constants{
	private Camera stageCamera;
	private OrthographicCamera worldCamera;
	private TiledStage tiledStage;
	private PlayerActor playerActor;
	private Body playerBody;
	private float cameraZoom = 1f;
	private float zoom = 1f;
	private float cameraZoomSpeed = 0.15f;
	private float cameraMoveSpeed = 3f;
	public static boolean beChange = false;
	public UpdateCamera(GameScreen gameScreen) {
		this.tiledStage = gameScreen.tiledStage;
		this.stageCamera = tiledStage.getCamera();
		this.worldCamera = tiledStage.getWorldCamera();
		this.playerActor = gameScreen.playerActor;
		playerBody = this.playerActor.body;
		
		update(0, true);
	}
	
	public void update(float delta,boolean init) {
		
		
		{//放大缩小
			float zoom = beChange ? cameraZoom : 1f;
			if (this.zoom != zoom) {
				if(this.zoom < zoom){
					this.zoom = Math.min(zoom, this.zoom + cameraZoomSpeed * delta);
				}else {
					this.zoom = Math.max(zoom, this.zoom - cameraZoomSpeed * delta);
				}
				
				worldCamera.zoom = this.zoom;
				((OrthographicCamera)stageCamera).zoom = this.zoom;
			}
		}
		
		{//相机的移动
			Vector2 playerVector2 = playerBody.getPosition();
			
			float worldX = playerVector2.x;//设置为玩家刚体的位置
			float worldY = playerVector2.y + playerActor.cacheDir*3;
			//世界相机大小的一半
			float halfWorldViewportHeight = worldCamera.viewportHeight*0.5f;
			float halfWorldViewportWidth = worldCamera.viewportWidth*0.5f;
			
				//	上下---------------
				if(worldX < halfWorldViewportWidth){  //防止在玩家落入坑中之后 世界相机继续跟随玩家
					worldX = halfWorldViewportWidth;
				}
				if( tiledStage.mapWidth > worldCamera.viewportWidth){//只有地图的高比相机的高大的时候才往上移动到距离最高点相机高的一半
					if (worldX > (tiledStage.mapPixelWidth - halfWorldViewportWidth * METERS_TO_PIXELS)* PIXELS_TO_METERS) {  //防止世界相机过高
						worldX =( tiledStage.mapPixelWidth  - halfWorldViewportWidth * METERS_TO_PIXELS)* PIXELS_TO_METERS;
					}
				}
				//上下-----------end
				
			//	左右--------------
			if(worldY < halfWorldViewportHeight){ //到达地图最右面
				worldY = halfWorldViewportHeight;
			}
			else if (worldY > (tiledStage.mapPixelHeight - halfWorldViewportHeight * METERS_TO_PIXELS)* PIXELS_TO_METERS) {//到达地图最左面
				worldY = ( tiledStage.mapPixelHeight - halfWorldViewportHeight * METERS_TO_PIXELS) * PIXELS_TO_METERS;
			}
			// 	左右---------end
//			if (MessageCenter.gameOver) {//当游戏结束，不再更新游戏的 Y 轴但是能更新 X 轴///这方法有问题，地形不动，其他元素会位移
//				worldY = worldCamera.position.y ;
//			}
			//更新世界相机的位置
			worldCamera.position.x +=  (worldX - worldCamera.position.x) * cameraMoveSpeed * delta;
			worldCamera.position.y +=  (worldY - worldCamera.position.y) * cameraMoveSpeed * delta;
		
			float x = worldCamera.position.x * METERS_TO_PIXELS;    //从物理世界位置坐标转化为舞台的像素坐标
			float y =worldCamera.position.y * METERS_TO_PIXELS;
			
			if(init){//初始的时候设置相机坐标
				worldCamera.position.set(worldX, worldY, worldCamera.position.z);//更新世界相机的位置
				x = worldX * METERS_TO_PIXELS;  
				y =  worldY * METERS_TO_PIXELS;
			}
			stageCamera.position.set(x,y,stageCamera.position.z);	//更新游戏舞台相机的位置
		}
	}
	
}
