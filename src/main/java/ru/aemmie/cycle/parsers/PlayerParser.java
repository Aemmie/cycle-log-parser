package ru.aemmie.cycle.parsers;

import org.apache.commons.lang3.EnumUtils;
import ru.aemmie.cycle.basic.Line;
import ru.aemmie.cycle.basic.Parser;
import ru.aemmie.cycle.events.parser.ParserEvent;
import ru.aemmie.cycle.events.parser.player.PlayerBodyCreatedEvent;
import ru.aemmie.cycle.events.parser.player.PlayerBodyDestroyedEvent;
import ru.aemmie.cycle.events.parser.player.PlayerFinishedEvent;
import ru.aemmie.cycle.events.parser.player.PlayerStateEvent;
import ru.aemmie.cycle.objects.GameMap;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static ru.aemmie.cycle.Utils.PLAYER_PREFIX;
import static ru.aemmie.cycle.events.parser.player.PlayerStateEvent.PlayerState.FINISHED;
import static ru.aemmie.cycle.events.parser.player.PlayerStateEvent.PlayerState.IN_MATCH;

public class PlayerParser implements Parser {

    @Override
    public String type() {
        return "LogYPlayer";
    }

    @Override
    public ParserEvent parse(Line line) {
        if (line.startsWith("OnRep_PlayerMatchState")) {
            var state = substringBetween(line.getMsg(), "[", "]");
            return PlayerStateEvent.builder()
                    .time(line.getTime())
                    .state("inMatch".equals(state) ? IN_MATCH : FINISHED)
                    .build();
        } else if (line.startsWith("OnPlayerStateChanged")) {
            String stateId = substringBetween(line.getMsg(), "PlayerState_Match_BP_C_", "'");
            return PlayerBodyCreatedEvent.builder()
                    .time(line.getTime())
                    //there are rare situations where Playerstate is 'None'
                    .stateId(stateId != null ? stateId : substringBetween(line.getMsg(), "Playerstate '", "'"))
                    .characterId(substringBetween(line.getMsg(), PLAYER_PREFIX, "'"))
                    .role(substringBetween(line.getMsg(), "Role '", "'"))
                    .map(GameMap.parse(substringBetween(line.getMsg(), "/Game/Maps/MP/", "/")))
                    .build();
        } else if (line.startsWith("AYPlayerCharacter::Destroyed()")) {
            return PlayerBodyDestroyedEvent.builder()
                    .time(line.getTime())
                    .characterId(substringAfter(line.getMsg(), PLAYER_PREFIX))
                    .build();
        } else if (line.startsWith("AYPlayerState::OnRep_PlayerMatchFinishedResult")) {
            var state = EnumUtils.getEnumIgnoreCase(PlayerFinishedEvent.PlayerState.class, substringBetween(line.getMsg(), "Result:", " "));
            var builder = PlayerFinishedEvent.builder()
                    .time(line.getTime())
                    .state(state);
            if (state == PlayerFinishedEvent.PlayerState.DEAD) {
                builder
                        .causer(substringBetween(line.getMsg(), "Damage:Causer:", " "))
                        .damage(Double.parseDouble(substringBetween(line.getMsg(), "m_healthDamage:", " ")))
                        .origin(substringBetween(line.getMsg(), "Origin:OriginRow:", " "));
            }
            return builder.build();

        }
        return null;
    }
}
