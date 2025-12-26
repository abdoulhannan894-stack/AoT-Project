package com.titan;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Registra los eventos para que el plugin funcione
        getServer().getPluginManager().registerEvents(this, this);
        
        // Crea automáticamente el mundo "titam" si no existe
        if(Bukkit.getWorld("titam") == null) {
            new WorldCreator("titam").type(WorldType.FLAT).createWorld();
        }
    }

    @EventHandler
    public void onODM(PlayerFishEvent e) {
        Player p = e.getPlayer();
        
        // Verifica si el jugador está usando la caña (anzuelo clavado o recogiendo)
        if (e.getState() == PlayerFishEvent.State.REEL_IN || e.getState() == PlayerFishEvent.State.IN_GROUND) {
            Location hookLoc = e.getHook().getLocation();
            
            // Calcula la dirección hacia el anzuelo
            Vector direction = hookLoc.toVector().subtract(p.getLocation().toVector());
            
            // Impulso estilo Equipo de Maniobras (AoT)
            // Multiplicador 3.8 para velocidad y un pequeño ajuste en Y para el salto
            p.setVelocity(direction.normalize().multiply(3.8).setY(direction.getY() * 0.1 + 1.2));
            
            // EFECTOS ESPECIALES (Versión 1.21+)
            // Sonido de ráfaga de viento (Wind Charge)
            p.playSound(p.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.5f, 1.3f);
            
            // Partículas de humo en el jugador
            p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation(), 20, 0.2, 0.2, 0.2, 0.05);
            
            // Partícula de impacto donde se clavó el anzuelo
            p.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, hookLoc, 1, 0, 0, 0, 0);
        }
    }
}
