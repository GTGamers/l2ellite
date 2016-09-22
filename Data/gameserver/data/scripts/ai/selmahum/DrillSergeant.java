package ai.selmahum;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.ai.Fighter;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.network.l2.s2c.SocialActionPacket;

public class DrillSergeant extends Fighter
{
	private static final int[] recruits = { 22780, 22782, 22783, 22784, 22785 };
	private long _wait_timeout = 0;

	public DrillSergeant(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ACTIVE_DELAY = 1000;
	}

	@Override
	public boolean thinkActive()
	{
		NpcInstance actor = getActor();

		if(System.currentTimeMillis() > _wait_timeout)
		{
			_wait_timeout = System.currentTimeMillis() + Rnd.get(10, 30) * 1000L;
			List<NpcInstance> around = actor.getAroundNpc(700, 100);
			if(around != null && !around.isEmpty())
				switch(Rnd.get(1, 3))
				{
					case 1:
						actor.broadcastPacket(new SocialActionPacket(actor.getObjectId(), 7));
						for(NpcInstance mob : around)
							if(ArrayUtils.contains(recruits, mob.getNpcId()))
								mob.broadcastPacket(new SocialActionPacket(mob.getObjectId(), 7));
						break;
					case 2:
						actor.broadcastPacket(new SocialActionPacket(actor.getObjectId(), 7));
						for(NpcInstance mob : around)
							if(ArrayUtils.contains(recruits, mob.getNpcId()))
								mob.broadcastPacket(new SocialActionPacket(mob.getObjectId(), 4));
						break;
					case 3:
						actor.broadcastPacket(new SocialActionPacket(actor.getObjectId(), 7));
						for(NpcInstance mob : around)
							if(ArrayUtils.contains(recruits, mob.getNpcId()))
								mob.broadcastPacket(new SocialActionPacket(mob.getObjectId(), 5));
						break;
				}
		}
		return false;
	}
}