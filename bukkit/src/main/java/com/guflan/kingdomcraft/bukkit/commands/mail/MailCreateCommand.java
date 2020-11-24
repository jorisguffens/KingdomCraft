package com.guflan.kingdomcraft.bukkit.commands.mail;

import com.guflan.kingdomcraft.api.domain.Kingdom;
import com.guflan.kingdomcraft.api.domain.Mail;
import com.guflan.kingdomcraft.api.domain.User;
import com.guflan.kingdomcraft.api.entity.PlatformPlayer;
import com.guflan.kingdomcraft.api.entity.PlatformSender;
import com.guflan.kingdomcraft.common.KingdomCraftImpl;
import com.guflan.kingdomcraft.common.command.CommandBase;
import com.guflan.kingdomcraft.common.ebean.beans.BMail;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MailCreateCommand extends CommandBase {
    public MailCreateCommand(KingdomCraftImpl kdc) {
        super(kdc, "mail create", 2, true);
        setArgumentsHint("<kingdom>");
        setExplanationMessage("cmdRanksCreateExplanation");
        setPermissions("kingdom.mails.create");
    }

    @Override
    public void execute(PlatformSender sender, String[] args) {
        Player player = (Player) sender;
        User user = kdc.getUser((PlatformPlayer) sender);
        Kingdom kingdom = user.getKingdom();
        if(isHoldingOwnBook(player)){

          BMail mail = new BMail();

          mail.setPriority(1);
          mail.setReceiver(kingdom.getName());
          mail.setSubject(((BookMeta) player.getInventory().getItemInHand().getItemMeta()).getTitle());

          List<String> bookContext =  ((BookMeta) player.getInventory().getItemInHand().getItemMeta()).getPages();

          String joinedBookContext = String.join("|", bookContext);

          mail.setContext(joinedBookContext);

          //Possibly remove book in hand.

          kdc.saveAsync(mail);
        }

    }

    private boolean isHoldingOwnBook(Player player){
        if(player.getInventory().getItemInHand() != null){
            return false;
            //TODO Message the player he isn't holding a book in his hand.
        }
        if(player.getInventory().getItemInHand().getType() == Material.WRITTEN_BOOK){
            return false;
            //TODO Message to player the item in hand is not a book
        }
        if(((BookMeta) player.getInventory().getItemInHand().getItemMeta()).getAuthor().equals(player.getName())){
            return false;
            //TODO Message the player the book needs to be written by the player.
        }
        return true;
    }


}
