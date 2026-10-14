# Squad system (design → code)

Implemented in **`minecraft-mod`** as an evolution of the **recruiter box** block id (`recruiter_box`).

## Rules

| Topic | Behavior |
| --- | --- |
| **Squad box** | Up to **5** soldiers; stores gear like old recruiter box |
| **Modes** | **Standard** / **Recruit** — GUI or `/villagers squad mode recruit\|standard` near box |
| **Assignment** | Reassignment writ: tag soldier → use on squad box; **shift** while using picks **leader** (else highest-kill member auto-leads) |
| **Leader** | Nametag **Leader ·** / **Acting leader ·**; squad box UI shows name; Sergeant+ = formal leader, else acting |
| **Beds** | Writ on bed within **128** blocks of box links bed to box (max **5**); writ soldier → bed assigns home |
| **Campaign** | **Shift + use** squad box or GUI toggles campaign. **Campaign boots removed.** Camp hold **32** blocks |
| **Nightwatch** | Squad box toggle only (**nightwatch helmet removed**) |
| **Post block** | **Removed** (legacy id breaks to air; use squad box + writ beds) |
| **Charge horn** | Only **on-campaign** soldiers within **128** blocks of commander |
| **Standing squad** | Soldiers with no squad box; no homeless desert while unassigned or on campaign |
| **Recruit fill** | **Recruit** squads with a leader: standing soldiers join; militia get box gear + soldier reqs → enlisted and assigned (64 blocks, path to leader) |
| **Ramparts** | Blank writ on up to **5** ramparts (coords stack on one writ); use on a **squad box** to link all (max **5** per box) |
| **Supply workers** | Nearest smith/fletcher/armorer within **24** blocks auto-link daily; weekly gear into box |

## Also in code

- **Squad box screen:** gear chest + counts + campaign / nightwatch / mode (no manual worker link buttons)
- **Attack plan** item (paper/ink/feather), GUI squad pick + target, use on **camp block**
- **Squad combat scan** (1 Hz) and **leader path follow**

## Still out of scope (Armies docs)

- Spawn tutorial camp NBT  
- Full recruiter villager job AI (emerald beds, mess routing)  
- Full siege ladders / sappers / boat plans (war stage cut)  
- Garland D2/O2 arc, orphanage prefab, roaming war bands
