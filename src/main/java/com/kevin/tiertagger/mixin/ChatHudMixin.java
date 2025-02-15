package com.kevin.tiertagger.mixin;

import com.kevin.tiertagger.TierTagger;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

import static com.kevin.tiertagger.TierTagger.getPlayerTier;
import static com.kevin.tiertagger.TierTagger.lastMsg;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @ModifyArg(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/util/ChatMessages;breakRenderedChatMessageLines(Lnet/minecraft/text/StringVisitable;ILnet/minecraft/client/font/TextRenderer;)Ljava/util/List;"),
            index = 0)
    public StringVisitable appendTierToRenderedMessages(StringVisitable message2){
        return getTier((Text) message2).append(((Text) message2).copy().styled(s -> s.withColor(0xFFFFFF)));
    }

    public MutableText getTier(Text content){
        return (TierTagger.getPlayerTier(lastMsg.gp().getName()) != null) ? TierTagger.getPlayerTier(lastMsg.gp().getName()) : Text.literal("UT: ");
    }
}

//Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I
//Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I